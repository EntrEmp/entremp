package com.entremp.core.entremp.api.review

data class ReviewDTO(
    val comment: String?,
    val qualifications: List<ReviewQualificationDTO>
) {
    constructor(): this(
            comment = "",
            qualifications = emptyList()
    )
}