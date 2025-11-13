package pl.srw.parentbank.data.datasource

import app.cash.sqldelight.db.SqlDriver

/**
 * Platform-specific database driver factory.
 * Implemented using expect/actual pattern.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
