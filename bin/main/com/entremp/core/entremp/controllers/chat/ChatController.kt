package com.entremp.core.entremp.controllers.chat

import com.entremp.core.entremp.api.chat.MessageDTO
import com.entremp.core.entremp.controllers.Authenticated
import com.entremp.core.entremp.data.chat.ChatRepository
import com.entremp.core.entremp.data.chat.MessageRepository
import com.entremp.core.entremp.model.chat.Chat
import com.entremp.core.entremp.model.chat.Message
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.support.JavaSupport.unwrap
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/chat")
class ChatController(
        private val chatRepository: ChatRepository,
        private val messageRepository: MessageRepository
): Authenticated {

    @GetMapping
    fun all(): Iterable<Chat> {
        val auth: User? = getAuthUser()

        return  chatRepository.findByBuyerOrProvider(auth, auth)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: String): Chat? {
        val auth: User? = getAuthUser()

        val chat: Chat? = chatRepository.findById(id).unwrap()

        if(auth!!.id != chat?.provider!!.id && auth.id != chat.buyer!!.id) {
            throw RuntimeException("Unauthorized: you have no access to this chat")
        }

        return chat
    }

    @PostMapping("/{id}")
    fun post(@PathVariable id: String, @RequestBody input: MessageDTO): Chat? {
        val auth: User? = getAuthUser()

        val chat: Chat? = chatRepository.findById(id).unwrap()

        if(auth!!.id != chat?.provider!!.id && auth.id != chat.buyer!!.id) {
            throw RuntimeException("Unauthorized: you have no access to this chat")
        }

        val receiver: User? = if(auth.id == chat.provider.id){
            chat.buyer
        } else {
            chat.provider
        }

        val message = Message(
                chat = chat,
                sender = auth,
                receiver = receiver,
                message = input.message,
                sent = DateTime.now(DateTimeZone.UTC)
        )

        messageRepository.save(message)

        return chatRepository.findById(id).unwrap()
    }

    //TODO agregar una duplicacion/actualizacion sobre la cotizacion asociada al chat (seria repetir el pedido)
    //TODO setear la muestra a false
}