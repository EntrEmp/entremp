package com.entremp.core.entremp.model.product

import java.util.*

data class ProductAttribute(
        val id: UUID,
        val productId: UUID,
        val attributeId: UUID,
        val active: Boolean
){
        companion object {
            fun create(
                    productId: UUID,
                    attributeId: UUID
            ): ProductAttribute {
                    return ProductAttribute(
                            id = UUID.randomUUID(),
                            productId = productId,
                            attributeId = attributeId,
                            active = true
                    )
            }
        }
}