package com.entremp.core.entremp.data.chat

import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.user.User
import org.springframework.data.repository.CrudRepository

interface ChatRepository: CrudRepository<Chat, String> {
    fun findByBuyerOrProvider(buyer: User?, provider: User?): Iterable<Chat>

    fun findByBuyer(buyer: User?): Iterable<Chat>
    fun findByProvider(provider: User?): Iterable<Chat>
}