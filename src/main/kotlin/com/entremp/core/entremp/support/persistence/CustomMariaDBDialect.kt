package com.entremp.core.entremp.support.persistence


import org.jetbrains.exposed.sql.vendors.DataTypeProvider
import org.jetbrains.exposed.sql.vendors.FunctionProvider
import org.jetbrains.exposed.sql.vendors.MysqlDialect

open class CustomMariaDBDialect: MysqlDialect() {
    override val dataTypeProvider: DataTypeProvider = CustomMysqlDataTypeProvider
    override val functionProvider : FunctionProvider = CustomMariaDBFunctionProvider()
    override val name: String = dialectName
    override val supportsOnlyIdentifiersInGeneratedKeys = true
    companion object {
        const val dialectName = "mariadb"
    }
}