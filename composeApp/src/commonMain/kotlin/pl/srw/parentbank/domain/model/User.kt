package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a user (parent or child).
 */
@Serializable
data class User(
    val id: String,
    val familyId: String,
    val name: String,
    val email: String? = null,  // Optional, mainly for parents
    val role: UserRole,
    val age: Int? = null,  // Optional, mainly for children
    val profilePictureUrl: String? = null,
    val createdAt: Long,
    val updatedAt: Long
)
