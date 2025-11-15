package pl.srw.parentbank.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import pl.srw.parentbank.data.datasource.DatabaseDriverFactory
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * Android-specific Koin module.
 * Provides platform-specific dependencies like database driver.
 */
actual fun platformModule(): Module = module {
    single {
        DatabaseDriverFactory.setContext(androidContext())
        val driver = DatabaseDriverFactory().createDriver()
        ParentBankDatabase(driver)
    }
}
