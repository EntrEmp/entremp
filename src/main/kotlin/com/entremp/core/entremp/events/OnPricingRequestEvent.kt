package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.pricing.Pricing
import com.entremp.core.entremp.model.product.Product
import com.entremp.core.entremp.model.user.User
import org.springframework.context.ApplicationEvent

data class OnPricingRequestEvent(
    val pricing: Pricing
): ApplicationEvent(pricing)
