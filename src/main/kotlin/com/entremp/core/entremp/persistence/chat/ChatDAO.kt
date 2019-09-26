package com.entremp.core.entremp.persistence.chat

import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class ChatDAO: TransactionSupport() {

    fun all(): List<Chat> = transaction {
        ChatEntity
            .all()
            .map { entity: ChatEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Chat = transaction {
        ChatEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Chat): Chat = transaction {
        ChatEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun delete(id: UUID) = transaction {
        ChatEntity[id].delete()
    }
}