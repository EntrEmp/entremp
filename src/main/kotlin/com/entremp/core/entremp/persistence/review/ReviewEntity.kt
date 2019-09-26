package com.entremp.core.entremp.persistence.review

import com.entremp.core.entremp.model.review.Review
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

class ReviewEntity(id: EntityID<UUID>)
    : AbstractEntity<Review>(id) {

    companion object: AbstractEntityClass<Review, ReviewEntity>(
        ReviewTable
    )

    var buyerId: UUID by ReviewTable.buyerId
    var providerId: UUID by ReviewTable.providerId
    var budgetId: UUID by ReviewTable.budgetId
    var comment: String by ReviewTable.comment
    var createdAt: DateTime by ReviewTable.createdAt
    var updatedAt: DateTime? by ReviewTable.updatedAt


    override fun create(source: Review): AbstractEntity<Review> {
        return update(source)
    }

    override fun update(source: Review): AbstractEntity<Review> {
        return this
    }

    override fun toDomainType(): Review {
        return Review(
            id = id.value,
            buyerId = buyerId,
            providerId = providerId,
            budgetId = budgetId,
            comment = comment,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}