package com.entremp.core.entremp.api.chat

data class MessageDTO(
        val message: String,
        val isQuestion: Boolean,
        val isAnswer: Boolean
)