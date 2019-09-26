package com.entremp.core.entremp.persistence.product.category

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ProductCategoryTable: UUIDTable(name = "product_category") {
    val productId: Column<UUID> = uuid(name = "product_id")
    val categoryId: Column<UUID> = uuid(name = "category_id")
}