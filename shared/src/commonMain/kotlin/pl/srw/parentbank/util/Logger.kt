package pl.srw.parentbank.util

import io.github.aakira.napier.Napier

/**
 * Logging utility with fail-fast error handling.
 * Wraps Napier for multiplatform logging.
 */
object Logger {

    /**
     * Log debug message.
     */
    fun debug(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.d(message, throwable, tag)
    }

    /**
     * Log info message.
     */
    fun info(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.i(message, throwable, tag)
    }

    /**
     * Log warning message.
     */
    fun warning(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.w(message, throwable, tag)
    }

    /**
     * Log error message.
     * For critical errors that should crash the app (fail-fast), use [fatal] instead.
     */
    fun error(message: String, tag: String? = null, throwable: Throwable? = null) {
        Napier.e(message, throwable, tag)
    }

    /**
     * Log fatal error and crash the app (fail-fast principle).
     * Use this for critical errors that leave the app in an invalid state.
     */
    fun fatal(message: String, throwable: Throwable? = null, tag: String? = null): Nothing {
        Napier.e("FATAL: $message", throwable, tag)
        throw IllegalStateException("Fatal error: $message", throwable)
    }

    /**
     * Assert a condition and crash if it fails (fail-fast).
     * Useful for validating invariants.
     */
    inline fun assert(condition: Boolean, lazyMessage: () -> String) {
        if (!condition) {
            fatal(lazyMessage())
        }
    }

    /**
     * Require a value to be non-null or crash (fail-fast).
     */
    fun <T : Any> requireNotNull(value: T?, lazyMessage: () -> String): T {
        return value ?: fatal(lazyMessage())
    }
}
