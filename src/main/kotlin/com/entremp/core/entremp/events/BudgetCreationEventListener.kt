package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.Product
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
data class BudgetCreationEventListener(
    val notificationService: NotificationService,
    val mailer: EmailService,
    val context: EntrEmpContext
) : ApplicationListener<OnBudgetRequestEvent> {

    private val factory: MustacheFactory = DefaultMustacheFactory()

    override fun onApplicationEvent(event: OnBudgetRequestEvent) {
        notify(event)
    }

    fun notify(event: OnBudgetRequestEvent){
        val budget: Budget = event.budget

        val pricing: Pricing? = budget.pricing
        val product: Product? = pricing?.product
        val buyer: User? = pricing?.buyer
        val provider: User? = pricing?.provider

        // Create Notification
        val message: String = """
            Tenes novedades respecto a tu solicitud de ${product?.name}.
            El proveedor ${provider?.name} te ha ofrecido un presupuesto.
            Estas a un paso de conseguir eso que estabas buscando!!!
        """.trimIndent()

        notificationService.save(
            user = buyer!!,
            type = "BUDGET",
            message = message
        )

        // Send Email
        val subject = "EntrEmp Presupuesto de Cotización"
        val server = context.toServerAddress()
        val pricingUrl = "$server/buyer/pricings/${pricing.id}"

        val buyerHome = "$server/buyer/home"
        val sellerHome = "$server/seller/home"

        val text = template(
            resource = "templates/mails/budget_requested.mustache",
            dataMap = mapOf(
                "pricingUrl" to pricingUrl,
                "buyerHome" to buyerHome,
                "sellerHome" to sellerHome,
                "emailAddress" to buyer.email,
                "providerName" to provider?.name,
                "productName" to product?.name
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