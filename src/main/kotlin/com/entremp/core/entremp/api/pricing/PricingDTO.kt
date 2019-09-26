package com.entremp.core.entremp.api.pricing

import com.entremp.core.entremp.model.commons.DeliveryTerm
import com.entremp.core.entremp.model.pricing.PricingStatus

class PricingDTO(
    val productId: String,

    val quantity: Long,

    val specifications: String,

    val deliveryTerm: DeliveryTerm = DeliveryTerm.IN_15_DAYS,

    val sample: Boolean = false,

    val status: PricingStatus = PricingStatus.PENDING
) {
    constructor(): this(
            productId = "",
            quantity = 0,
            specifications = ""
    )
}