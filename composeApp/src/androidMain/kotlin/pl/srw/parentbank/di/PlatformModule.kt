package pl.srw.parentbank.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.srw.parentbank.db.ParentBankDatabase

actual fun platformModule(): Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = ParentBankDatabase.Schema,
            context = get(),
            name = "parentbank.db"
        )
    }
}
