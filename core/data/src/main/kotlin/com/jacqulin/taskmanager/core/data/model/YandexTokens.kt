package com.jacqulin.taskmanager.core.data.model

data class YandexTokens(
    val iamToken: String,
    val folderId: String
) {
    val isConfigured: Boolean
        get() = iamToken.isNotBlank() && folderId.isNotBlank()
}
