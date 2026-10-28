package com.jacqulin.taskmanager.core.storage.security

interface SecureTokenStorage {
    fun getToken(name: String): String?
    fun setToken(name: String, value: String)
    fun removeToken(name: String)
}