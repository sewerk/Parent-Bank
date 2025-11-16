package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.dsl.module
import pl.srw.parentbank.data.datasource.DatabaseDriverFactory
import pl.srw.parentbank.db.ParentBankDatabase

/**
 * iOS-specific Koin module.
 * Provides platform-specific dependencies like database driver.
 */
actual fun platformModule(): Module = module {
    single {
        val driver = DatabaseDriverFactory().createDriver()
        ParentBankDatabase(driver)
    }
}
