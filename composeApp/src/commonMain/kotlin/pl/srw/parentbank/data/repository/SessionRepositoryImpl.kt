package pl.srw.parentbank.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.UserSession
import pl.srw.parentbank.domain.repository.SessionRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of SessionRepository.
 * Stores user session as JSON in a key-value store.
 */
class SessionRepositoryImpl(
    private val database: ParentBankDatabase
) : SessionRepository {

    private val queries = database.sessionQueries
    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val SESSION_KEY = "current_user_session"
    }

    override suspend fun saveSession(session: UserSession): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            val sessionJson = json.encodeToString(session)
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.insertOrReplace(
                key = SESSION_KEY,
                value_ = sessionJson,
                updatedAt = currentTime
            )
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to save session: ${e.message}", e)
            )
        }
    }

    override suspend fun getSession(): Outcome<UserSession?> = withContext(Dispatchers.Default) {
        try {
            val sessionJson = queries.selectByKey(SESSION_KEY).executeAsOneOrNull()
            if (sessionJson != null) {
                val session = json.decodeFromString<UserSession>(sessionJson)
                Outcome.Success(session)
            } else {
                Outcome.Success(null)
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get session: ${e.message}", e)
            )
        }
    }

    override suspend fun clearSession(): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            queries.deleteByKey(SESSION_KEY)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to clear session: ${e.message}", e)
            )
        }
    }

    override fun observeSession(): Flow<UserSession?> {
        return queries.selectByKey(SESSION_KEY)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { sessionJson ->
                sessionJson?.let {
                    try {
                        json.decodeFromString<UserSession>(it)
                    } catch (e: Exception) {
                        null
                    }
                }
            }
    }
}
