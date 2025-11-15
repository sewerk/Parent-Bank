package pl.srw.parentbank.di

import org.koin.core.module.Module

/**
 * Platform-specific module (expect/actual pattern).
 * Will be implemented in androidMain and iosMain.
 */
expect fun platformModule(): Module
