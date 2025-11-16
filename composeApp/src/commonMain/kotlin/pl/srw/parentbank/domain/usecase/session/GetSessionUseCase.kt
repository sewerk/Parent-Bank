package pl.srw.parentbank.domain.usecase.session

import pl.srw.parentbank.domain.model.UserSession
import pl.srw.parentbank.domain.repository.SessionRepository
import pl.srw.parentbank.domain.usecase.UseCase
import pl.srw.parentbank.util.Outcome

/**
 * Use case for retrieving stored user session.
 * Returns null if no session exists.
 */
class GetSessionUseCase(
    private val sessionRepository: SessionRepository
) : UseCase<UserSession?>() {

    override suspend fun execute(): Outcome<UserSession?> {
        return sessionRepository.getSession()
    }
}
