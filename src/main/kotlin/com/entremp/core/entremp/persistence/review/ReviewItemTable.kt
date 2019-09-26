package com.entremp.core.entremp.persistence.review

import com.entremp.core.entremp.model.review.ReviewType
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ReviewItemTable: UUIDTable(name = "review_item") {
    val reviewId: Column<UUID> = uuid("review_id")
    val reviewType: Column<ReviewType> = enumerationByName(
        name = "review_type",
        length = 15,
        klass = ReviewType::class
    )
    val qualification: Column<Double> = double("qualification")
}