package com.entremp.core.entremp.api.chat

data class ShowMessageDTO(
    val message: String,
    val styleClass: String,
    val avatar: String
)