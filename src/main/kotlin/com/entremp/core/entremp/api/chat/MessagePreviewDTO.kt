package com.entremp.core.entremp.api.chat

data class MessagePreviewDTO(
    val name: String,
    val date: String,
    val message: String,
    val chatId: String,
    val role: String
){
    fun previewMessage(): String {
        return if(message.length > 47){
            "${message.substring(0, 44)}..."
        } else {
            message
        }
    }
}