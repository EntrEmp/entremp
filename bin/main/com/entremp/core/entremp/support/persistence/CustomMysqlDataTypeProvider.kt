package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.vendors.DataTypeProvider
import org.jetbrains.exposed.sql.vendors.DatabaseDialect
import java.util.*

object CustomMysqlDataTypeProvider : DataTypeProvider() {
    override fun dateTimeType(): String = if ((currentDialect as CustomMysqlDialect).isFractionDateTimeSupported()) "DATETIME(6)" else "DATETIME"

    override fun uuidToDB(value: UUID): Any = value.toString()

    override fun uuidType(): String = "VARCHAR(36)"
}

internal val currentDialect: DatabaseDialect get() = TransactionManager.current().db.dialect

