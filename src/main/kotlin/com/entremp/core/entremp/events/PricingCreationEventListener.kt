package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.user.User
import com.entremp.core.entremp.service.NotificationService
import com.entremp.core.entremp.support.EmailService
import com.entremp.core.entremp.support.EntrEmpContext
import com.entremp.core.entremp.support.templates.TemplateBuilder
import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
data class PricingCreationEventListener(
    val notificationService: NotificationService,
    val mailer: EmailService,
    val context: EntrEmpContext
) : ApplicationListener<OnPricingRequestEvent> {

    private val factory: MustacheFactory = DefaultMustacheFactory()

    override fun onApplicationEvent(event: OnPricingRequestEvent) {
        notify(event)
    }

    fun notify(event: OnPricingRequestEvent){
        val pricing: Pricing = event.pricing

        val buyer: User? = pricing.buyer
        val provider: User? = pricing.provider
        val product: String? = pricing.product?.name

        // Create Notification
        val message: String = """
            El usuario ${buyer?.name} ha solicitado una cotización de tu producto $product!
        """.trimIndent()

        notificationService.save(
            user = provider!!,
            type = "PRICING",
            message = message
        )

        // Send Email
        val subject = "EntrEmp Solicitud de Cotización"
        val server = context.toServerAddress()
        val pricingUrl = "$server/seller/pricings/${pricing.id}"

        val text = template(
            resource = "templates/mails/pricing_requested.mustache",
            dataMap = mapOf(
                "pricingUrl" to pricingUrl,
                "emailAddress" to provider.email
            )
        )

        mailer.sendMail(
            recipients = listOf(provider.email),
            subject = subject,
            text = text
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