package com.entremp.core.entremp.model.product

import java.util.*

data class ProductCategory(
        val id: UUID,
        val productId: UUID,
        val categoryId: UUID
) {
        companion object {
            fun create(
                    productId: UUID,
                    categoryId: UUID
            ): ProductCategory {
                    return ProductCategory(
                            id = UUID.randomUUID(),
                            productId = productId,
                            categoryId = categoryId
                    )
            }
        }
}