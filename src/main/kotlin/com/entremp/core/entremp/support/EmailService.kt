package com.entremp.core.entremp.support

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage

@Service
class EmailService(
        private val sender: JavaMailSender
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun sendMail(
            recipients: List<String>,
            subject: String,
            text: String
    ) {

        logger.info("Sending email with subject $subject")

        val message = SimpleMailMessage()
        message.setTo(*recipients.toTypedArray())
        message.setSubject(subject)
        message.setText(text)

        try {
            sender.send(message)
        } catch (throwable: Throwable){
            logger.error("Could not sent email", throwable)
        }
    }

    fun sendMailWithAttachement(
            recipients: List<String>,
            subject: String,
            html: String,
            attachment: String
    ) {
        val message: MimeMessage = sender.createMimeMessage()

        // Multipart must be activated (true)
        val helper = MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        )

        helper.setTo(recipients.toTypedArray())
        helper.setSubject(subject)
        helper.setText(html, true)

        val resource = ClassPathResource(attachment)
        val attachmentName: String = resource.filename?: ""
        helper.addAttachment(attachmentName, resource)

        sender.send(message)
    }
}