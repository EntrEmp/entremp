package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

class DataSourceInitializer(
    private val config: DataSourceConfig,
    private val tables: List<Table>
) : TransactionSupport() {

    companion object {
        private const val DROP_ACK: String = "Yes, please delete all my data forever."
    }

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    val isTest: Boolean = config.isTest

    fun initAuto(dropSql: String? = null) = transaction {
        if (config.drop == DROP_ACK && dropSql != null) {
            exec(ClassPathResource(dropSql))
        }

        SchemaUtils.create(*tables.toTypedArray())
        SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
    }

    fun dropIfRequired(dropSql: String) {
        if (config.drop == DROP_ACK) {
            logger.info("Dropping existing database")
            exec(ClassPathResource(dropSql))
        }
    }

    fun initIfRequired(
        scriptsPath: String
    ) {
        logger.info("Initializing database")

        PathMatchingResourcePatternResolver().getResources(
            scriptsPath
        ).sortedBy { resource ->
            resource.filename
        }.filter { resource ->
            resource.filename?.matches(Regex("^\\d+-.*")) ?: false
        }.forEach { resource ->
            logger.info("Executing DDL: ${resource.filename}")
            exec(resource)
        }
    }

    fun exec(resource: Resource) {
        transaction {
            val sql: String = resource.inputStream.bufferedReader().readText()
            sql.split(";").filter { line ->
                line.isNotBlank()
            }.forEach { line ->
                exec("$line;")
            }
        }
    }
}
