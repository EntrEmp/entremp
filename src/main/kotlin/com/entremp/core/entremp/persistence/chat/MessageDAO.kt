package com.entremp.core.entremp.persistence.chat

import com.entremp.core.entremp.model.chat.Message
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class MessageDAO: TransactionSupport() {

    fun all(): List<Message> = transaction {
        MessageEntity
            .all()
            .map { entity: MessageEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Message = transaction {
        MessageEntity[id].toDomainType()
    }


    fun saveOrUpdate(source: Message): Message = transaction {
        MessageEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }

    fun delete(id: UUID) = transaction {
        MessageEntity[id].delete()
    }
}