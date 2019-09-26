package com.entremp.core.entremp.model.review

import java.util.*

data class ReviewItem(
    val id: UUID,
    val reviewId: UUID,
    val reviewType: ReviewType,
    val qualification: Double
) {
        companion object {
            fun qualifyDeliveryTerm(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewItem {
                    return ReviewItem(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            reviewType = ReviewType.DELIVERY_TERM,
                            qualification = qualification
                    )
            }

            fun qualifyProduct(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewItem {
                    return ReviewItem(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            reviewType = ReviewType.PRODUCT_QUALITY,
                            qualification = qualification
                    )
            }

            fun qualifyService(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewItem {
                    return ReviewItem(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            reviewType = ReviewType.SERVICE_QUALITY,
                            qualification = qualification
                    )
            }
        }
}