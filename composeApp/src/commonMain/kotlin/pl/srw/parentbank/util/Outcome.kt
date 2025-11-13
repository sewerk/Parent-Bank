package pl.srw.parentbank.util

/**
 * A sealed class representing the outcome of an operation.
 * Follows the fail-fast principle for error handling.
 */
sealed class Outcome<out T> {
    data class Success<T>(val data: T) : Outcome<T>()
    data class Failure(val exception: AppException) : Outcome<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Failure -> throw exception
    }

    inline fun onSuccess(action: (T) -> Unit): Outcome<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onFailure(action: (AppException) -> Unit): Outcome<T> {
        if (this is Failure) action(exception)
        return this
    }
}

inline fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> {
    return when (this) {
        is Outcome.Success -> Outcome.Success(transform(data))
        is Outcome.Failure -> Outcome.Failure(exception)
    }
}

inline fun <T, R> Outcome<T>.flatMap(transform: (T) -> Outcome<R>): Outcome<R> {
    return when (this) {
        is Outcome.Success -> transform(data)
        is Outcome.Failure -> Outcome.Failure(exception)
    }
}
