package pl.srw.parentbank.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * JVM-specific Koin module.
 * Provides platform-specific dependencies for JVM target (mainly for testing).
 */
actual fun platformModule(): Module = module {
    // JVM-specific dependencies will be added here
    // Example: single { DatabaseDriverFactory() }
}
