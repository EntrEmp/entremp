package com.entremp.core.entremp.persistence.pricing

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object PricingAttachements: UUIDTable(name = "pricing_attachements") {
    val pricingId: Column<UUID> = uuid(name = "pricing_id")
    val fileLocation: Column<String> = varchar(
            name = "file_location",
            length = 255
    )
    val createdAt: Column<DateTime> = datetime(name = "created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}