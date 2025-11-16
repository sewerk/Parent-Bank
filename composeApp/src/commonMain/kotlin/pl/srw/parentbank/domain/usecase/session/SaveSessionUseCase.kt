package pl.srw.parentbank.domain.usecase.session

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserSession
import pl.srw.parentbank.domain.repository.SessionRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.Outcome

/**
 * Use case for saving user session after login/registration.
 */
class SaveSessionUseCase(
    private val sessionRepository: SessionRepository
) : UseCaseWithParams<User, Unit>() {

    override suspend fun execute(params: User): Outcome<Unit> {
        val session = UserSession(
            userId = params.id,
            familyId = params.familyId,
            userName = params.name,
            userRole = params.role,
            lastLoginAt = Clock.System.now().toEpochMilliseconds()
        )
        return sessionRepository.saveSession(session)
    }
}
