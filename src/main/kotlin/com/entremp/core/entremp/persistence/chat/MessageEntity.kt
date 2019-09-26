package com.entremp.core.entremp.persistence.chat

import com.entremp.core.entremp.model.chat.Message
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class MessageEntity(id: EntityID<UUID>)
    : AbstractEntity<Message>(id) {

    companion object: AbstractEntityClass<Message, MessageEntity>(
        MessageTable
    )

    var chatId: UUID by MessageTable.chatId
    var senderId: UUID by MessageTable.senderId
    var receiverId: UUID by MessageTable.receiverId
    var message: String by MessageTable.message
    var sent: DateTime by MessageTable.sent
    var seen: Boolean by MessageTable.seen
    var createdAt: DateTime by MessageTable.createdAt
    var updatedAt: DateTime? by MessageTable.updatedAt

    override fun create(source: Message): AbstractEntity<Message> {
        return update(source)
    }

    override fun update(source: Message): AbstractEntity<Message> {
        chatId = source.chatId
        senderId = source.senderId
        receiverId = source.receiverId
        message = source.message
        sent = source.sent
        seen = source.seen
        createdAt = source.createdAt
        updatedAt = source.updatedAt

        return this
    }

    override fun toDomainType(): Message {
        return Message(
            id = id.value,
            chatId = chatId,
            senderId = senderId,
            receiverId = receiverId,
            message = message,
            sent = sent,
            seen = seen,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}