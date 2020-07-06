package com.entremp.core.entremp.support

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage


@Service
class EmailService(
        private val sender: JavaMailSender,
        private val sendGrid: SendGrid
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun sendMail(
            recipients: List<String>,
            subject: String,
            text: String
    ) {

        logger.info("Sending email with subject $subject")

        val mime: MimeMessage = sender.createMimeMessage()
        val helper = MimeMessageHelper(mime, true)

        helper.setTo(recipients.toTypedArray())
        helper.setSubject(subject)
        helper.setText(text, true)

        try {
            sender.send(mime)
        } catch (throwable: Throwable){
            logger.error("Could not sent email", throwable)
        }
    }

    fun sendMailWithSendgrid(
        recipients: List<String>,
        subject: String,
        text: String
    ) {

        logger.info("Sending email with subject $subject")

        recipients
            .map { recipient ->
                Email(recipient)
            }
            .forEach { recipient ->
                val message = Mail(
                    Email("bitness.test@gmail.com"),
                    subject,
                    recipient,
                    Content("text/plain", text)
                )

                val request = Request()
                try {
                    request.method = Method.POST
                    request.endpoint = "mail/send"
                    request.body = message.build()

                    val response = sendGrid.api(request)

                    System.out.println(response.statusCode)
                    System.out.println(response.body)
                    System.out.println(response.headers)
                } catch (throwable: IOException) {
                    logger.error("Could not sent email", throwable)
                }
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