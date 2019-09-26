package com.entremp.core.entremp.persistence.review

import com.entremp.core.entremp.model.budget.Budget
import com.entremp.core.entremp.model.review.Review
import com.entremp.core.entremp.support.persistence.TransactionSupport
import java.util.*

class ReviewDAO: TransactionSupport() {

    fun all(): List<Review> = transaction {
        ReviewEntity
            .all()
            .map { entity: ReviewEntity ->
                entity.toDomainType()
            }
    }

    fun findById(id: UUID): Review = transaction {
        ReviewEntity[id].toDomainType()
    }

    fun findByBudget(source: Budget): Review? = transaction {
        ReviewEntity
            .find {
                ReviewTable.budgetId eq source.id
            }
            .map { entity ->
                entity.toDomainType()
            }
            .singleOrNull()
    }


    fun saveOrUpdate(source: Review): Review = transaction {
        ReviewEntity.saveOrUpdate(
            id = source.id,
            source = source
        )
    }
}