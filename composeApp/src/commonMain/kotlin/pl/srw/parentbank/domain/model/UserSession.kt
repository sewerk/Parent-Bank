package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a user session stored locally.
 * Used for persisting login state across app restarts.
 */
@Serializable
data class UserSession(
    val userId: String,
    val familyId: String,
    val userName: String,
    val userRole: UserRole,
    val lastLoginAt: Long
)
