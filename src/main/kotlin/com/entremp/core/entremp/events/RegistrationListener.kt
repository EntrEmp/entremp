package com.entremp.core.entremp.events

import com.entremp.core.entremp.data.user.VerificationTokenRepository
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.model.user.VerificationToken
import com.entremp.core.entremp.support.EmailService
import com.entremp.core.entremp.support.EntrEmpContext
import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
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

    private val factory: MustacheFactory = DefaultMustacheFactory()

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

        val buyerHome = "$server/buyer/home"
        val sellerHome = "$server/seller/home"

        val text = template(
            resource = "templates/mails/validate.mustache",
            dataMap = mapOf(
                "validateUrl" to url,
                "buyerHome" to buyerHome,
                "sellerHome" to sellerHome,
                "emailAddress" to recipientAddress
            )
        )

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

    private fun template(resource: String, dataMap: Map<String, Any?>): String =
        TemplateBuilder(
            templateName = resource,
            factory = factory
        )
            .data(dataMap)
            .build()
}