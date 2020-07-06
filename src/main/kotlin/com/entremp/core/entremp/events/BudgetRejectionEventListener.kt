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
data class BudgetRejectionEventListener(
    val notificationService: NotificationService,
    val mailer: EmailService,
    val context: EntrEmpContext
) : ApplicationListener<OnBudgetRejectEvent> {

    private val factory: MustacheFactory = DefaultMustacheFactory()

    override fun onApplicationEvent(event: OnBudgetRejectEvent) {
        notify(event)
    }

    fun notify(event: OnBudgetRejectEvent){
        val budget: Budget = event.budget

        val pricing: Pricing? = budget.pricing

        val buyer: User? = pricing?.buyer
        val provider: User? = pricing?.provider

        // Create Notification
        val message: String = """
            Lamentamos informarte que el cliente ${buyer?.name} no aprobo tu cotización, pero EntrEmp sigue siendo el mejor lugar para vender a otras empresas!
        """.trimIndent()

        notificationService.save(
            user = provider!!,
            type = "BUDGET",
            message = message
        )

        // Send Email
        val subject = "EntrEmp Presupuesto Rechazado"
        val server = context.toServerAddress()
        val pricingUrl = "$server/seller/pricings/${pricing.id}"

        val buyerHome = "$server/buyer/home"
        val sellerHome = "$server/seller/home"

        val text = template(
            resource = "templates/mails/budget_rejected.mustache",
            dataMap = mapOf(
                "pricingUrl" to pricingUrl,
                "buyerHome" to buyerHome,
                "sellerHome" to sellerHome,
                "buyerName" to buyer?.name
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