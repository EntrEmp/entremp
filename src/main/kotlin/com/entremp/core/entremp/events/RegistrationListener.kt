package com.entremp.core.entremp.events

import com.entremp.core.entremp.data.user.VerificationTokenRepository
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.model.user.VerificationToken
import com.entremp.core.entremp.support.EmailService
import com.entremp.core.entremp.support.EntrEmpContext
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.MessageSource
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component
import java.util.*

@Component
data class RegistrationListener(
    val verificationTokenRepository: VerificationTokenRepository,
    val mailer: EmailService,
    val context: EntrEmpContext
): ApplicationListener<OnRegistrationCompleteEvent> {

    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) {
        confirmRegistration(event)
    }

    private fun confirmRegistration(event: OnRegistrationCompleteEvent){
        val user: User = event.user
        val token = UUID.randomUUID().toString()
        createVerificationToken(user, token)

        val recipientAddress: String = user.email
        val subject = "EntrEmp Confirmación de Cuenta"
        val server = context.toServerAddress()
        val url = "$server/confirm?token=$token"


        val text =
            """
            Gracias por registrarte en EntrEmp, a continuación le pasamos el link de confirmación.
            $url
            """.trimIndent()

        mailer.sendMail(
            recipients = listOf(recipientAddress),
            subject = subject,
            text = text
        )
    }

    private fun createVerificationToken(
        user: User,
        token: String) {
        verificationTokenRepository.save(
            VerificationToken(
                user = user,
                token = token,
                expiration = DateTime.now().plusHours(24)
            )
        )
    }
}