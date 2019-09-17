package com.entremp.core.entremp.model.review

import java.util.*

data class Review(
        val id: UUID,
        val buyerId: UUID,
        val providerId: UUID,
        val budgetId: UUID,
        val comment: String
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
                    comment = comment
            )
        }
    }
}