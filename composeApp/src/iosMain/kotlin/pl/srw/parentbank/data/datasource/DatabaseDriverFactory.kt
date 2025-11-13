package pl.srw.parentbank.data.datasource

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * iOS implementation of the database driver factory.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ParentBankDatabase.Schema,
            name = "parentbank.db"
        )
    }
}
