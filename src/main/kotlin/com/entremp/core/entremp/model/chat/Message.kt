package com.entremp.core.entremp.model.chat

import org.joda.time.DateTime
import java.util.*

data class Message(
        val id: UUID,
        val chatId: UUID,
        val senderId: UUID,
        val receiverId: UUID,
        val message: String,
        val sent: DateTime,
        val seen: Boolean,
        val createdAt: DateTime,
        val updatedAt: DateTime?
) {
        companion object {
            fun create(
               chatId: UUID,
               senderId: UUID,
               receiverId: UUID,
               message: String
            ): Message {
                val now = DateTime.now()
                    return Message(
                            id = UUID.randomUUID(),
                            chatId = chatId,
                            senderId = senderId,
                            receiverId = receiverId,
                            message = message,
                            sent = now,
                            seen = false,
                        createdAt = now,
                        updatedAt = null
                    )
            }
        }
}