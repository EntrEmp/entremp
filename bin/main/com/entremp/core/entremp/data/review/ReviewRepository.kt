package com.entremp.core.entremp.data.review

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.review.Review
import org.springframework.data.repository.CrudRepository

interface ReviewRepository: CrudRepository<Review, String> {
    fun findByBudget(budget: Budget?): Review?
}