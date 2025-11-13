package pl.srw.parentbank.util

/**
 * Base exception class for all application errors.
 * Follows the fail-fast principle - errors are explicit and propagated immediately.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {

    // Network errors (for future Firebase integration)
    data class NetworkError(val errorMessage: String) : AppException(errorMessage)

    // Authentication errors
    data class AuthError(val errorMessage: String) : AppException(errorMessage)

    // Validation errors
    data class ValidationError(val field: String, val errorMessage: String) :
        AppException("Validation failed for $field: $errorMessage")

    // Business logic errors
    data class BusinessError(val errorMessage: String) : AppException(errorMessage)

    // Database errors
    data class DatabaseError(val errorMessage: String, val originalCause: Throwable? = null) :
        AppException(errorMessage, originalCause)

    // Data sync errors (for future Firebase integration)
    data class SyncError(val errorMessage: String) : AppException(errorMessage)

    // Unknown errors
    data class UnknownError(val originalCause: Throwable) :
        AppException("An unexpected error occurred: ${originalCause.message}", originalCause)
}
