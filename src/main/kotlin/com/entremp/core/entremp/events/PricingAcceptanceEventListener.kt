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
data class PricingAcceptanceEventListener(
    val notificationService: NotificationService,
    val mailer: EmailService,
    val context: EntrEmpContext
) : ApplicationListener<OnPricingAcceptEvent> {

    private val factory: MustacheFactory = DefaultMustacheFactory()

    override fun onApplicationEvent(event: OnPricingAcceptEvent) {
        notify(event)
    }

    fun notify(event: OnPricingAcceptEvent){
        val pricing: Pricing = event.pricing

        val buyer: User? = pricing.buyer
        val provider: User? = pricing.provider

        // Create Notification
        val message: String = """
            Gracias por revolucionar el canal de ventas Entre Empresas con nosotros.
            Tu proveedoor ${provider?.name} aprobo tu cotización.  
            Estas a un click de obtener eso que estas buscando!
        """.trimIndent()

        notificationService.save(
            user = buyer!!,
            type = "PRICING",
            message = message
        )

        // Send Email
        val subject = "EntrEmp Cotización Aceptada"
        val server = context.toServerAddress()
        val pricingUrl = "$server/buyer/pricings/${pricing.id}"

        val buyerHome = "$server/buyer/home"
        val sellerHome = "$server/seller/home"

        val text = template(
            resource = "templates/mails/pricing_accepted.mustache",
            dataMap = mapOf(
                "pricingUrl" to pricingUrl,
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