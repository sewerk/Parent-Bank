package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * iOS-specific Koin module.
 * Provides platform-specific dependencies like database driver.
 */
actual fun platformModule(): Module = module {
    // iOS-specific dependencies will be added here
    // Example: single { createIOSDriver() }
}
