package com.entremp.core.entremp.model.chat

import org.joda.time.DateTime
import java.util.*

data class Chat(
        val id: UUID,
        val buyerId: UUID,
        val providerId: UUID,
        val createdAt: DateTime,
        val updatedAt: DateTime?
) {
        companion object {
            fun create(
                    buyerId: UUID,
                    providerId: UUID
            ): Chat {
                    return Chat(
                        id = UUID.randomUUID(),
                        buyerId = buyerId,
                        providerId = providerId,
                        createdAt = DateTime.now(),
                        updatedAt = null
                    )
            }
        }
}