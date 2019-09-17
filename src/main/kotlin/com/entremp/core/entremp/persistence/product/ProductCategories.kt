package com.entremp.core.entremp.persistence.product

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ProductCategories: UUIDTable(name = "product_categories") {
    val productId: Column<UUID> = uuid(name = "product_id")
    val categoryId: Column<UUID> = uuid(name = "category_id")
}