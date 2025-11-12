package pl.srw.parentbank.data.datasource

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * Android implementation of the database driver factory.
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ParentBankDatabase.Schema,
            context = context,
            name = "parentbank.db"
        )
    }
}
