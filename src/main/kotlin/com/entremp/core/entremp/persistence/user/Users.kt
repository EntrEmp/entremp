package com.entremp.core.entremp.persistence.user

import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime

object Users: UUIDTable(name = "users")
{
    val email: Column<String> = varchar(
            name = "email",
            length = 100
    )

    val password: Column<String> = varchar(
            name = "password",
            length = 255
    )

    val token: Column<String> = varchar(
            name = "token",
            length = 36
    )

    val address: Column<String> = varchar(
            name = "phone",
            length = 255
    )

    val name: Column<String> = varchar(
            name = "phone",
            length = 100
    )

    val cuit: Column<Long> = long(
            name = "cuit"
    )

    val phone: Column<String> = varchar(
            name = "phone",
            length = 20
    )

    val active: Column<Boolean> = bool(
            name = "active"
    )

    val createdAt: Column<DateTime> = datetime("created_at")

    val updatedAt: Column<DateTime?> = date(name = "updated_at").nullable()
}