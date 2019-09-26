package com.entremp.core.entremp.api.review

import com.entremp.core.entremp.model.review.ReviewType

data class ReviewQualificationDTO(
    val reviewType: ReviewType?,
    val qualification: Double?
){
    constructor(): this(
            reviewType = null,
            qualification = 0.0
    )
}