package com.entremp.core.entremp.controllers

import com.entremp.core.entremp.api.support.ContactDTO
import com.entremp.core.entremp.support.EmailService
import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class ContactController(
    private val mailer: EmailService
) {
    private val factory: MustacheFactory = DefaultMustacheFactory()

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val contactEmail: String = "info@entremp.com"

    @RequestMapping("contact",
        method = [RequestMethod.POST])
    fun register(
        @RequestBody prospect: ContactDTO,
        request: HttpServletRequest
    ): ResponseEntity<Map<String,String?>> {

        return try {
            val role: String = if(prospect.seller){
                "Vendedor"
            } else {
                "Comprador"
            }

            val text = template(
                resource = "templates/mails/contact.mustache",
                dataMap = mapOf(
                    "clientEmail" to prospect.email,
                    "name" to prospect.name,
                    "phone" to prospect.phone,
                    "cuit" to prospect.cuit,
                    "role" to role,
                    "criteria" to prospect.criteria
                )
            )

            mailer.sendMail(
                recipients = listOf(contactEmail),
                subject = "Nuevo Cliente EntrEmp",
                text = text
            )

            ResponseEntity
                .ok(
                    mapOf("result" to "OK")
                )
        }
        catch (throwable: Throwable) {
            logger.error("Error during contact", throwable)
            ResponseEntity
                .status(500)
                .body(
                    mapOf(
                        "result" to "ERROR",
                        "cause" to throwable.message
                    )
                )
        }

    }

    private fun template(resource: String, dataMap: Map<String, Any?>): String =
        TemplateBuilder(
            templateName = resource,
            factory = factory
        )
            .data(dataMap)
            .build()

}