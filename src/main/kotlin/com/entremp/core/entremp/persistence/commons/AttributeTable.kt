package com.entremp.core.entremp.persistence.commons

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.*

object AttributeTable: UUIDTable(name = "attribute") {
    val categoryId: Column<UUID> = uuid("category_id")
    val name: Column<String> = varchar(
        name = "name",
        length = 100
    )
}