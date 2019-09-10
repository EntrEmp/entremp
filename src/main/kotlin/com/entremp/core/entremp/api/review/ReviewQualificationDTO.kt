package com.entremp.core.entremp.api.review

import com.entremp.core.entremp.model.review.QualificationType

data class ReviewQualificationDTO(
        val qualificationType: QualificationType?,
        val qualification: Double?
){
    constructor(): this(
            qualificationType = null,
            qualification = 0.0
    )
}