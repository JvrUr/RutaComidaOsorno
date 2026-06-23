package osornogourmet.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    fun connect() {
        // En Neon, la URL ya viene con SSL activado. Ej: jdbc:postgresql://ep-xxx.neon.tech/dbname?sslmode=require
        val url = System.getenv("DB_URL") ?: "jdbc:postgresql://ep-quiet-poetry-ade85o8t-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require"
        val user = System.getenv("DB_USER") ?: "neondb_owner"
        val password = System.getenv("DB_PASSWORD") ?: "npg_BRUwv8lSZod2"

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = url
            username = user
            this.password = password
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}
