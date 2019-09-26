package com.entremp.core.entremp.persistence.product.image

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object ProductImageTable: UUIDTable(name = "product_images") {
    val productId: Column<UUID> = uuid(name = "product_id")
    val fileLocation: Column<String> = varchar(
            name = "file_location",
            length = 255
    )
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}