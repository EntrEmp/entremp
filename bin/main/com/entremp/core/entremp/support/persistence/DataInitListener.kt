package com.entremp.core.entremp.support.persistence

import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

class DataInitListener(
    private val dataSourceInitializer: DataSourceInitializer
) {

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        dataSourceInitializer.dropIfRequired(
            if (dataSourceInitializer.isTest) {
                "/db/drop.h2.sql"
            } else {
                "/db/drop.sql"
            }
        )

        if (!dataSourceInitializer.isTest) {
            transaction {
                exec("USE snowden;")
            }
        }

        if (dataSourceInitializer.isTest) {
            dataSourceInitializer.initAuto("/db/drop.h2.sql")
        } else {
            dataSourceInitializer.initIfRequired("classpath:/db/*.sql")
        }
    }
}