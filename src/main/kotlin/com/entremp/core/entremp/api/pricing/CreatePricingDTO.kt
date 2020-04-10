package com.entremp.core.entremp.api.pricing

import com.entremp.core.entremp.model.DeliveryTerm

data class CreatePricingDTO(
    val productId: String,
    val quantity: Long,
    val sample: Boolean = false,
    val deliveryTerm: DeliveryTerm,
    val specifications: String
)