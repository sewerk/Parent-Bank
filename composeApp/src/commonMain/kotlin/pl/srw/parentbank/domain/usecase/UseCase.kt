package pl.srw.parentbank.domain.usecase

import pl.srw.parentbank.util.Outcome

/**
 * Base class for use cases with no parameters.
 */
abstract class UseCase<out T> {
    suspend operator fun invoke(): Outcome<T> = execute()
    protected abstract suspend fun execute(): Outcome<T>
}

/**
 * Base class for use cases with a single parameter.
 */
abstract class UseCaseWithParams<in P, out T> {
    suspend operator fun invoke(params: P): Outcome<T> = execute(params)
    protected abstract suspend fun execute(params: P): Outcome<T>
}
