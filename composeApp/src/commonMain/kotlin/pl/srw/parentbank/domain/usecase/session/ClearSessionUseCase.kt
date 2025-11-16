package pl.srw.parentbank.domain.usecase.session

import pl.srw.parentbank.domain.repository.SessionRepository
import pl.srw.parentbank.domain.usecase.UseCase
import pl.srw.parentbank.util.Outcome

/**
 * Use case for clearing user session (logout).
 */
class ClearSessionUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<Unit>() {

    override suspend fun execute(): Outcome<Unit> {
        return sessionRepository.clearSession()
    }
}
