package pl.srw.parentbank.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.srw.parentbank.domain.model.UserSession
import pl.srw.parentbank.util.Outcome

/**
 * Repository interface for managing user session persistence.
 */
interface SessionRepository {
    suspend fun saveSession(session: UserSession): Outcome<Unit>
    suspend fun getSession(): Outcome<UserSession?>
    suspend fun clearSession(): Outcome<Unit>
    fun observeSession(): Flow<UserSession?>
}
