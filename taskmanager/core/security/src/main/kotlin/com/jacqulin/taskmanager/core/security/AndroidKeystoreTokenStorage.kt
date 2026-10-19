package com.jacqulin.taskmanager.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.GeneralSecurityException
import java.security.KeyStore
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AndroidKeystoreTokenStorage @Inject constructor(
    @ApplicationContext context: Context,
) : SecureTokenStorage {
    private val storageDirectory = File(context.filesDir, STORAGE_DIRECTORY).apply {
        if (!exists() && !mkdirs()) {
            throw IOException("Unable to create secure token directory")
        }
    }

    override fun getToken(name: String): String? {
        val file = fileFor(name)
        if (!file.exists()) return null

        return try {
            val storedBytes = file.readBytes()
            require(storedBytes.size > IV_SIZE) { "Invalid encrypted token data" }
            val iv = storedBytes.copyOfRange(0, IV_SIZE)
            val ciphertext = storedBytes.copyOfRange(IV_SIZE, storedBytes.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(TAG_SIZE_BITS, iv))
            cipher.updateAAD(name.toByteArray(Charsets.UTF_8))
            cipher.doFinal(ciphertext).toString(Charsets.UTF_8)
        } catch (exception: GeneralSecurityException) {
            if (exception !is AEADBadTagException) throw exception
            file.delete()
            null
        } catch (exception: IllegalArgumentException) {
            file.delete()
            null
        }
    }

    override fun setToken(name: String, value: String) {
        if (value.isEmpty()) {
            removeToken(name)
            return
        }
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        cipher.updateAAD(name.toByteArray(Charsets.UTF_8))
        val encrypted = cipher.iv + cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        val target = fileFor(name)
        val temporary = File(storageDirectory, "${target.name}.tmp")
        try {
            temporary.writeBytes(encrypted)
            try {
                Files.move(
                    temporary.toPath(),
                    target.toPath(),
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING,
                )
            } catch (exception: AtomicMoveNotSupportedException) {
                Files.move(temporary.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        } finally {
            temporary.delete()
        }
    }

    override fun removeToken(name: String) {
        val file = fileFor(name)
        if (file.exists() && !file.delete()) {
            throw IOException("Unable to remove encrypted token")
        }
    }

    private fun fileFor(name: String): File {
        require(name.isNotBlank()) { "Token name must not be blank" }
        val encodedName = Base64.encodeToString(
            name.toByteArray(Charsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING,
        )
        return File(storageDirectory, encodedName)
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build(),
        )
        return keyGenerator.generateKey()
    }

    private companion object {
        const val ANDROID_KEY_STORE = "AndroidKeyStore"
        const val KEY_ALIAS = "taskmanager_ai_api_tokens"
        const val STORAGE_DIRECTORY = "secure_tokens"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val IV_SIZE = 12
        const val TAG_SIZE_BITS = 128
    }
}
