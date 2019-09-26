package com.entremp.core.entremp.persistence.chat

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object ChatTable: UUIDTable(name = "chat") {
    val buyerId: Column<UUID> = uuid("buyer_id")
    val providerId: Column<UUID> = uuid("provider_id")
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date("updated_at").nullable()
}