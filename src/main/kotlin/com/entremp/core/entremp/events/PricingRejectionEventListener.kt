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
data class PricingRejectionEventListener(
    val notificationService: NotificationService,
    val mailer: EmailService,
    val context: EntrEmpContext
) : ApplicationListener<OnPricingRejectEvent> {

    private val factory: MustacheFactory = DefaultMustacheFactory()

    override fun onApplicationEvent(event: OnPricingRejectEvent) {
        notify(event)
    }

    fun notify(event: OnPricingRejectEvent){
        val pricing: Pricing = event.pricing

        val buyer: User? = pricing.buyer
        val provider: User? = pricing.provider

        // Create Notification
        val message: String = """
            Lamentamos informarte que el proveedoor ${provider?.name} no aprobo tu cotización. Seguí buscando!
        """.trimIndent()

        notificationService.save(
            user = buyer!!,
            type = "PRICING",
            message = message
        )

        // Send Email
        val subject = "EntrEmp Cotización Rechazada"
        val server = context.toServerAddress()
        val pricingUrl = "$server/buyer/pricings/${pricing.id}"
        val productsUrl = "$server/buyer/products"

        val buyerHome = "$server/buyer/home"
        val sellerHome = "$server/seller/home"

        val text = template(
            resource = "templates/mails/pricing_rejected.mustache",
            dataMap = mapOf(
                "pricingUrl" to pricingUrl,
                "productsUrl" to productsUrl,
                "buyerHome" to buyerHome,
                "sellerHome" to sellerHome,
                "emailAddress" to buyer.email,
                "providerName" to provider?.name
            )
        )

        mailer.sendMail(
            recipients = listOf(buyer.email),
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