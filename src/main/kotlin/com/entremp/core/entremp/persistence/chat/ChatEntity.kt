package com.entremp.core.entremp.persistence.chat

import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class ChatEntity(id: EntityID<UUID>)
    : AbstractEntity<Chat>(id) {

    companion object: AbstractEntityClass<Chat, ChatEntity>(
        ChatTable
    )

    var buyerId: UUID by ChatTable.buyerId
    var providerId: UUID by ChatTable.providerId
    var createdAt: DateTime by ChatTable.createdAt
    var updatedAt: DateTime? by ChatTable.updatedAt

    override fun create(source: Chat): AbstractEntity<Chat> {
        return update(source)
    }

    override fun update(source: Chat): AbstractEntity<Chat> {
        buyerId = source.buyerId
        providerId = source.providerId
        createdAt = source.createdAt
        updatedAt = source.updatedAt
        return this
    }

    override fun toDomainType(): Chat {
        return Chat(
            id = id.value,
            buyerId = buyerId,
            providerId = providerId,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}