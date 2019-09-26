package com.entremp.core.entremp.persistence.certification

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object CertificationTable: UUIDTable(name = "certification") {
    val userId: Column<UUID> = uuid(name = "user_id")
    val name: Column<String> = varchar(
            name = "name",
            length = 100
    )
    val fileLocation: Column<String> = varchar(
            name = "file_location",
            length = 255
    )
    val createdAt: Column<DateTime> = datetime(name = "created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}