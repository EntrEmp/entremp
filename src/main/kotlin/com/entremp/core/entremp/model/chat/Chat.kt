package com.entremp.core.entremp.model.chat

import java.util.*

data class Chat(
        val id: UUID,
        val buyerId: UUID,
        val providerId: UUID
) {
        companion object {
            fun create(
                    buyerId: UUID,
                    providerId: UUID
            ): Chat {
                    return Chat(
                            id = UUID.randomUUID(),
                            buyerId = buyerId,
                            providerId = providerId
                    )
            }
        }
}