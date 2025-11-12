package pl.srw.parentbank.data.datasource

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.JdbcSqliteDriver
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * JVM implementation of the database driver factory.
 * Uses JDBC SQLite driver for testing and desktop applications.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // Using in-memory SQLite database for JVM target
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ParentBankDatabase.Schema.create(driver)
        return driver
    }
}
