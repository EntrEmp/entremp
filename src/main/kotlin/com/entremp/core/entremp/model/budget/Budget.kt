package com.entremp.core.entremp.model.budget

import com.entremp.core.entremp.model.commons.DeliveryTerm
import org.joda.time.DateTime
import java.util.*


data class Budget(
    val id: UUID,
    val pricingId: UUID,
    val price: Double,
    val iva: Double,
    val deliveryConditions: String,
    val paymentConditions: String,
    val specifications: String,
    val deliveryTerm: DeliveryTerm,
    val createdAt: DateTime,
    val updatedAt: DateTime?
) {
        companion object {
            fun create(
                    pricingId: UUID,
                    price: Double,
                    iva: Double,
                    deliveryConditions: String,
                    paymentConditions: String,
                    specifications: String
            ): Budget {
                    return Budget(
                            id = UUID.randomUUID(),
                            pricingId = pricingId,
                            price = price,
                            iva = iva,
                            deliveryConditions = deliveryConditions,
                            paymentConditions = paymentConditions,
                            specifications = specifications,
                            deliveryTerm = DeliveryTerm.IN_15_DAYS,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }

            fun create(
                    pricingId: UUID,
                    price: Double,
                    iva: Double,
                    deliveryConditions: String,
                    paymentConditions: String,
                    specifications: String,
                    deliveryTerm: DeliveryTerm
            ): Budget {
                    return Budget(
                            id = UUID.randomUUID(),
                            pricingId = pricingId,
                            price = price,
                            iva = iva,
                            deliveryConditions = deliveryConditions,
                            paymentConditions = paymentConditions,
                            specifications = specifications,
                            deliveryTerm = deliveryTerm,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}