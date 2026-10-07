package com.jacqulin.taskmanager.core.voice.data.model

data class SpeechField(
    val key: String,
    val label: String,
    val placeholder: String = "",
    val isSecret: Boolean = true,
)

data class SpeechProviderInfo(
    val id: String,
    val name: String,
    val description: String,
    val fields: List<SpeechField>,
)

object SpeechProviderCatalog {
    val providers = listOf(
        SpeechProviderInfo(
            id = "gpt4o-transcribe",
            name = "GPT-4o mini Transcribe",
            description = "OpenAI · Быстрое многоязычное распознавание",
            fields = listOf(
                SpeechField("token", "API-токен", "sk-...", isSecret = true),
            ),
        ),
        SpeechProviderInfo(
            id = "nova-3",
            name = "Nova-3",
            description = "Deepgram · Низкая задержка и высокая точность",
            fields = listOf(
                SpeechField("token", "API-токен", "dg-...", isSecret = true),
            ),
        ),
        SpeechProviderInfo(
            id = "universal-2",
            name = "Universal-2",
            description = "AssemblyAI · Устойчиво распознаёт длинную речь",
            fields = listOf(
                SpeechField("token", "API-токен", "...", isSecret = true),
            ),
        ),
        SpeechProviderInfo(
            id = "salute-speech",
            name = "SaluteSpeech",
            description = "Сбер · Оптимизировано для русского языка",
            fields = listOf(
                SpeechField("token", "API-токен", "...", isSecret = true),
            ),
        ),
        SpeechProviderInfo(
            id = "yandex-speech",
            name = "Yandex Speech",
            description = "Yandex · Русский, потоковое распознавание",
            fields = listOf(
                SpeechField("iamToken", "IAM-токен", "t1.…",  isSecret = true),
                SpeechField("folderId", "Folder ID",  "b1g…",  isSecret = true),
            ),
        ),
    )

    fun byId(id: String): SpeechProviderInfo? = providers.firstOrNull { it.id == id }
}