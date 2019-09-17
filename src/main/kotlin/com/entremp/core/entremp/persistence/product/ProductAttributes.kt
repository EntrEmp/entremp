package com.entremp.core.entremp.persistence.product

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object ProductAttributes: UUIDTable(name = "product+attributes") {
    val productId: Column<UUID> = uuid(name = "product_id")
    val attributeId: Column<UUID> = uuid(name = "attribute_id")
    val active: Column<Boolean> = bool("active")
}