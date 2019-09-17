package com.entremp.core.entremp.model.review

import java.util.*

data class ReviewQualification(
        val id: UUID,
        val reviewId: UUID,
        val qualificationType: QualificationType,
        val qualification: Double
) {
        companion object {
            fun qualifyDeliveryTerm(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewQualification {
                    return ReviewQualification(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            qualificationType = QualificationType.DELIVERY_TERM,
                            qualification = qualification
                    )
            }

            fun qualifyProduct(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewQualification {
                    return ReviewQualification(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            qualificationType = QualificationType.PRODUCT_QUALITY,
                            qualification = qualification
                    )
            }

            fun qualifyService(
                    reviewId: UUID,
                    qualification: Double
            ): ReviewQualification {
                    return ReviewQualification(
                            id = UUID.randomUUID(),
                            reviewId = reviewId,
                            qualificationType = QualificationType.SERVICE_QUALITY,
                            qualification = qualification
                    )
            }
        }
}