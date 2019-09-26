package com.entremp.core.entremp.model.review

import org.joda.time.DateTime
import java.util.*

data class Review(
        val id: UUID,
        val buyerId: UUID,
        val providerId: UUID,
        val budgetId: UUID,
        val comment: String,
        val createdAt: DateTime,
        val updatedAt: DateTime?
) {
    companion object {
        fun create(
                buyerId: UUID,
                providerId: UUID,
                budgetId: UUID,
                comment: String
        ): Review {
            return Review(
                id = UUID.randomUUID(),
                buyerId = buyerId,
                providerId = providerId,
                budgetId = budgetId,
                comment = comment,
                createdAt = DateTime.now(),
                updatedAt = null
            )
        }
    }
}