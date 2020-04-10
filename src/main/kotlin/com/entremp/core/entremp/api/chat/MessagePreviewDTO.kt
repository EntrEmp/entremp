package com.entremp.core.entremp.api.chat

data class MessagePreviewDTO(
    val name: String,
    val date: String,
    val message: String,
    val chatId: String,
    val role: String
)