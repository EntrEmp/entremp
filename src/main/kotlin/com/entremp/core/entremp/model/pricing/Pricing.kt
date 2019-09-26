package com.entremp.core.entremp.model.pricing

import com.entremp.core.entremp.model.commons.DeliveryTerm
import org.joda.time.DateTime
import java.util.*


data class Pricing(
    val id: UUID,
    val buyerId: UUID,
    val providerId: UUID,
    val productId: UUID,
    val quantity: Long,
    val specifications: String,
    val sample: Boolean,
    val deliveryTerm: DeliveryTerm,
    val status: PricingStatus,
    val createdAt: DateTime,
    val updatedAt: DateTime?
) {
        companion object {
            fun create(
                    buyerId: UUID,
                    providerId: UUID,
                    productId: UUID,
                    quantity: Long,
                    specifications: String
            ): Pricing {
                    return Pricing(
                            id = UUID.randomUUID(),
                            buyerId = buyerId,
                            providerId = providerId,
                            productId = productId,
                            quantity = quantity,
                            specifications = specifications,
                            sample = false,
                            deliveryTerm = DeliveryTerm.IN_15_DAYS,
                            status = PricingStatus.PENDING,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }

                fun create(
                    buyerId: UUID,
                    providerId: UUID,
                    productId: UUID,
                    quantity: Long,
                    specifications: String,
                    sample: Boolean,
                    deliveryTerm: DeliveryTerm
            ): Pricing {
                    return Pricing(
                            id = UUID.randomUUID(),
                            buyerId = buyerId,
                            providerId = providerId,
                            productId = productId,
                            quantity = quantity,
                            specifications = specifications,
                            sample = sample,
                            deliveryTerm = deliveryTerm,
                            status = PricingStatus.PENDING,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}