package com.entremp.core.entremp.service

import com.entremp.core.entremp.data.chat.ChatRepository
import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.chat.Message
import com.entremp.core.entremp.model.user.User

import com.entremp.core.entremp.support.JavaSupport.unwrap

class ChatService(val chatRepository: ChatRepository) {

    private val buyerRole = "buyer"
    private val sellerRole = "seller"

    fun findChats(user: User,
                  role: String): List<Chat> {

        val chats: Iterable<Chat> = when(role){
            buyerRole ->
                chatRepository.findByBuyer(
                    buyer = user
                )

            sellerRole ->
                chatRepository.findByProvider(
                    provider = user
                )

            else ->
                chatRepository.findByBuyerOrProvider(
                    buyer = user,
                    provider = user
                )

        }

        return chats.distinctBy { chat ->
            chat.id
        }
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