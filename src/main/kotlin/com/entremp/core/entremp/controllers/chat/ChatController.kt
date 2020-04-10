package com.entremp.core.entremp.controllers.chat

import com.entremp.core.entremp.api.chat.CreateMessageDTO
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import javax.servlet.http.HttpServletRequest


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
    fun post(@PathVariable id: String,
             @ModelAttribute request: CreateMessageDTO,
             servletRequest: HttpServletRequest,
             redirectAttributes: RedirectAttributes
    ): RedirectView {
        val auth: User = getAuthUser() !!

        val chat: Chat? = chatRepository
            .findById(id)
            .unwrap()

        if(auth.id != chat?.provider!!.id || auth.id != chat.buyer!!.id) {
            throw RuntimeException("Unauthorized: you have no access to this chat")
        } else {
            val message = Message(
                chat = chat,
                sender = auth.id!!,
                role = request.role,
                message = request.message,
                sent = DateTime.now(DateTimeZone.UTC)
            )

            messageRepository.save(message)

            return RedirectView("/web/${request.role}/messages/$id")
        }
    }

    //TODO agregar una duplicacion/actualizacion sobre la cotizacion asociada al chat (seria repetir el pedido)
    //TODO setear la muestra a false
}