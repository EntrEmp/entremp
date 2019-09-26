package com.entremp.core.entremp.persistence.review

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object ReviewTable: UUIDTable(name = "review") {
    val buyerId: Column<UUID> = uuid("buyer_id")
    val providerId: Column<UUID> = uuid("provider_id")
    val budgetId: Column<UUID> = uuid("budget_id")
    val comment: Column<String> = varchar(
        name = "comment",
        length = 255
    )
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date("created_at").nullable()
}