package com.entremp.core.entremp.support.persistence

data class DataSourceConfig(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
    val logStatements: Boolean,
    val drop: String,
    val initOnStart: Boolean
) {
    val isTest: Boolean = driver == "org.h2.Driver"
}
