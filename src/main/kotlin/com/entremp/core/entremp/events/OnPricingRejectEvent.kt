package com.entremp.core.entremp.events

import com.entremp.core.entremp.model.pricing.Pricing
import org.springframework.context.ApplicationEvent

data class OnPricingRejectEvent(
    val pricing: Pricing
): ApplicationEvent(pricing)