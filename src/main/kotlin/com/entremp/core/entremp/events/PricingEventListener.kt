package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.notification.Notification
import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.service.NotificationService
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
data class PricingEventListener(
    val notificationService: NotificationService
) : ApplicationListener<OnPricingRequestEvent> {

    override fun onApplicationEvent(event: OnPricingRequestEvent) {
        notify(event)
    }

    fun notify(event: OnPricingRequestEvent){
        val pricing: Pricing = event.pricing

        val message: String = """
            El usuario ${event.pricing.buyer?.name} está interesado en tu producto ${event.pricing.product?.name}!
        """.trimIndent()

        notificationService.save(
            user = pricing.provider!!,
            type = "PRICING",
            message = message
        )
    }
}