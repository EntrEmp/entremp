package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Connection.TRANSACTION_REPEATABLE_READ

abstract class TransactionSupport {

    @Autowired
    private lateinit var config: DataSourceConfig

    @Autowired
    private lateinit var db: Database

    fun<T> transaction(
        isolationLevel: Int = TRANSACTION_REPEATABLE_READ,
        repetitionAttempts: Int = 1,
        statement: Transaction.() -> T
    ): T {
        return org.jetbrains.exposed.sql.transactions.transaction(
            transactionIsolation = isolationLevel,
            repetitionAttempts = repetitionAttempts,
            db = db
        ) {
            if (config.logStatements) {
                addLogger(StdOutSqlLogger)
            }

            statement()
        }
    }
}
