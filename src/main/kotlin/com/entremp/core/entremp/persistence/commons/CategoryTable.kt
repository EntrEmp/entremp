package com.entremp.core.entremp.persistence.commons

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column

object CategoryTable: UUIDTable(name = "category") {
    val name: Column<String> = varchar(
        name = "name",
        length = 100
    )
}