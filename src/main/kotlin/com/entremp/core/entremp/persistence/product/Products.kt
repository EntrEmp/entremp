package com.entremp.core.entremp.persistence.product

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object Products: UUIDTable(name = "products") {
    val userId: Column<UUID> = uuid(name = "user_id")
    val name: Column<String> = varchar(
            name = "name",
            length = 255
    )
    val minimum: Column<Int> = integer(name = "minimum")
    val maximum: Column<Int> = integer(name = "maximum")
    val batchSize: Column<Int> = integer(name = "batch_size")
    val description: Column<String> = varchar(
            name = "description",
            length = 255
    )
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}