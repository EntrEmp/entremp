package com.entremp.core.entremp.api.budget

import com.entremp.core.entremp.model.DeliveryTerm

class BudgetDTO(
        val pricingId: String,
        val price: Double,
        val iva: Double,

        val deliveryTerm: DeliveryTerm = DeliveryTerm.IN_15_DAYS,

        val deliveryConditions: String,
        val paymentConditions: String,
        val specifications: String
) {
    constructor(): this(
            pricingId = "",
            price = 0.0,
            iva = 0.0,
            deliveryConditions = "",
            paymentConditions = "",
            specifications = ""
    )
}