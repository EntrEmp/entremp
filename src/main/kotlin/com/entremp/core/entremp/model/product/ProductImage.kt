package com.entremp.core.entremp.model.product

import com.entremp.core.entremp.model.Fileable
import org.joda.time.DateTime
import java.util.*

data class ProductImage(
        val id: UUID,
        val productId: UUID,
        val fileLocation: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
): Fileable(fileLocation) {
        companion object {
            fun create(
                    productId: UUID,
                    fileLocation: String
            ): ProductImage {
                    return ProductImage(
                            id = UUID.randomUUID(),
                            productId = productId,
                            fileLocation = fileLocation,
                            createdAt = DateTime.now(),
                            updatedAt = null
                    )
            }
        }
}