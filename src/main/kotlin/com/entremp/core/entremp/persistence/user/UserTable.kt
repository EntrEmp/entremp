package com.entremp.core.entremp.persistence.user

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object UserTable: UUIDTable(name = "user") {
    val email: Column<String> = varchar(
            name = "email",
            length = 100
    )
    val password: Column<String> = varchar(
            name = "password",
            length = 255
    )
    val token: Column<UUID?> = uuid(name = "token").nullable()
    val address: Column<String> = varchar(
            name = "address",
            length = 255
    )
    val name: Column<String> = varchar(
            name = "name",
            length = 100
    )
    val cuit: Column<Long> = long(name = "cuit")
    val phone: Column<String> = varchar(
            name = "phone",
            length = 20
    )
    val active: Column<Boolean> = bool(name = "active")
    val createdAt: Column<DateTime> = datetime("created_at")
    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}