import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jacqulin.taskmanager.core.voice"
    compileSdk {
        version = release(37)
    }

    buildFeatures {
        buildConfig = true
    }

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    val yandexFolderId = localProperties.getProperty("YANDEX_FOLDER_ID", "")
    val yandexIamToken = localProperties.getProperty("YANDEX_IAM_TOKEN", "")

    defaultConfig {
        minSdk = 26
        buildConfigField("String", "YANDEX_FOLDER_ID", "\"$yandexFolderId\"")
        buildConfigField("String", "YANDEX_IAM_TOKEN", "\"$yandexIamToken\"")
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // Okhttp
    implementation(libs.okhttp.logging.interceptor)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
}