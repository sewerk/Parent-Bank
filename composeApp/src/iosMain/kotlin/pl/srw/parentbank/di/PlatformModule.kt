package pl.srw.parentbank.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.srw.parentbank.db.ParentBankDatabase

actual fun platformModule(): Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            schema = ParentBankDatabase.Schema,
            name = "parentbank.db"
        )
    }
}
