package com.entremp.core.entremp.data.chat

import com.entremp.core.entremp.model.chat.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository: CrudRepository<Message, String>