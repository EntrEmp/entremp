package com.entremp.core.entremp.model.product

import org.joda.time.DateTime
import java.util.*

data class Product(
        val id: UUID,
        val userId: UUID,
        val name: String,
        val minimum: Int,
        val maximum: Int,
        val batchSize: Int,
        val description: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
) {
    companion object {
        fun create(
                userId: UUID,
                name: String,
                minimum: Int,
                maximum: Int,
                batchSize: Int,
                description: String
        ): Product {
            return Product(
                    id = UUID.randomUUID(),
                    userId = userId,
                    name = name,
                    minimum = minimum,
                    maximum = maximum,
                    batchSize = batchSize,
                    description = description,
                    createdAt = DateTime.now(),
                    updatedAt = null
            )
        }
    }
}