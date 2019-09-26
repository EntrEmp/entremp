package com.entremp.core.entremp.persistence.review

import com.entremp.core.entremp.model.review.ReviewItem
import com.entremp.core.entremp.model.review.ReviewType
import com.entremp.core.entremp.support.persistence.AbstractEntity
import com.entremp.core.entremp.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import java.util.*

class ReviewItemEntity(id: EntityID<UUID>)
    : AbstractEntity<ReviewItem>(id) {

    companion object: AbstractEntityClass<ReviewItem, ReviewItemEntity>(
        ReviewItemTable
    )

    var reviewId: UUID by ReviewItemTable.reviewId
    var reviewType: ReviewType by ReviewItemTable.reviewType
    var qualification: Double by ReviewItemTable.qualification

    override fun create(source: ReviewItem): AbstractEntity<ReviewItem> {
        return update(source)
    }

    override fun update(source: ReviewItem): AbstractEntity<ReviewItem> {
        reviewId = source.reviewId
        reviewType = source.reviewType
        qualification = source.qualification

        return this
    }

    override fun toDomainType(): ReviewItem {
        return ReviewItem(
            id = id.value,
            reviewId = reviewId,
            reviewType = reviewType,
            qualification = qualification
        )
    }
}