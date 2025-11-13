package pl.srw.parentbank.data.datasource

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * Android implementation of the database driver factory.
 *
 * Note: The Context must be set via setContext() before calling createDriver().
 * This is typically done during application initialization.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val context = requireNotNull(appContext) {
            "Context not initialized. Call DatabaseDriverFactory.setContext() first."
        }
        return AndroidSqliteDriver(
            schema = ParentBankDatabase.Schema,
            context = context,
            name = "parentbank.db"
        )
    }

    companion object {
        private var appContext: Context? = null

        /**
         * Initialize the factory with application context.
         * Should be called once during app initialization.
         */
        fun setContext(context: Context) {
            appContext = context.applicationContext
        }
    }
}
