package com.entremp.core.entremp.persistence.budget.attachement

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object BudgetAttachementTable: UUIDTable(name = "budget_attachement") {
    val budgetId: Column<UUID> = uuid(name = "budgetId")
    val fileLocation: Column<String> = varchar(
            name = "file_location",
            length = 255
    )
    val createdAt: Column<DateTime> = datetime(name = "created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}