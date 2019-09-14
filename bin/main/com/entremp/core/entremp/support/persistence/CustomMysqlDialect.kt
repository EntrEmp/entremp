package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.vendors.DataTypeProvider
import org.jetbrains.exposed.sql.vendors.MysqlDialect

open class CustomMysqlDialect: MysqlDialect() {
    override val dataTypeProvider: DataTypeProvider = CustomMysqlDataTypeProvider

    companion object {
        const val dialectName = "mysql"
    }
}