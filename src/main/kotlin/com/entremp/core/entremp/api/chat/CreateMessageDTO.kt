package com.entremp.core.entremp.api.chat

data class CreateMessageDTO(
    val message: String,
    val role: String
)