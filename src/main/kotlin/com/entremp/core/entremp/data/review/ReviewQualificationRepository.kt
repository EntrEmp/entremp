package com.entremp.core.entremp.data.review

import com.entremp.core.entremp.model.review.ReviewItem
import org.springframework.data.repository.CrudRepository

interface ReviewQualificationRepository: CrudRepository<ReviewItem, String>