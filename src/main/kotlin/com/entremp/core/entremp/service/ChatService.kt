package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.chat.ChatRepository
import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.chat.Message
import com.entremp.core.entremp.model.user.User

import com.entremp.core.entremp.support.JavaSupport.unwrap

class ChatService(val chatRepository: ChatRepository) {

    fun findChats(user: User, role: String): List<Chat> {
        return chatRepository.findByBuyerOrProvider(
            buyer = user,
            provider = user
        ).distinct()
    }

    fun getMessages(chatId: String): List<Message> {
        val chat: Chat? = chatRepository
            .findById(chatId)
            .unwrap()

        val messages: List<Message> = chat
            ?.let { mainChat: Chat ->
                mainChat.messages
            }
            ?: emptyList()

        return messages.sortedBy { message ->
            message.sent.millis
        }
    }
}