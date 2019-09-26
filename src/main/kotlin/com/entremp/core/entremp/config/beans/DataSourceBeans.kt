package com.entremp.core.entremp.config.beans


import com.entremp.core.entremp.persistence.certification.CertificationDAO
import com.entremp.core.entremp.persistence.certification.CertificationTable
import com.entremp.core.entremp.persistence.user.UserDAO
import com.entremp.core.entremp.persistence.user.UserTable
import com.entremp.core.entremp.support.persistence.*
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.jetbrains.exposed.sql.Database
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.support.BeanDefinitionDsl


object DataSourceBeans{
    fun beans(): BeanDefinitionDsl = org.springframework.context.support.beans {
        val mainConfig: Config = ConfigFactory.load()

        bean {
            val dbConfig: Config = mainConfig.getConfig("db")

            DataSourceConfig(
                url = dbConfig.getString("url"),
                user = dbConfig.getString("user"),
                password = dbConfig.getString("password"),
                driver = dbConfig.getString("driver"),
                logStatements = dbConfig.getBoolean("log-statements"),
                drop = dbConfig.getString("drop"),
                initOnStart = dbConfig.getBoolean("init-on-start")
            )
        }

        bean {
            Database.registerDialect(CustomMysqlDialect.dialectName) { CustomMysqlDialect() }
            Database.registerDialect(CustomMariaDBDialect.dialectName) { CustomMariaDBDialect() }

            Database.connect(
                datasource = ref()
            )
        }

        bean {
            val dataSourceConfig: DataSourceConfig = ref()

            HikariDataSource().apply {
                jdbcUrl = dataSourceConfig.url
                username = dataSourceConfig.user
                password = dataSourceConfig.password
                driverClassName = dataSourceConfig.driver
            }
        }

        // Database
        bean {
            DataSourceInitializer(
                config = ref(),
                tables = listOf(
                    UserTable,
                    CertificationTable
                )
            )
        }

        bean {
            DataInitListener(
                dataSourceInitializer = ref()
            )
        }
        bean {
            UserDAO()
        }
        bean {
            CertificationDAO()
        }
    }
}
