package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a child's account.
 * Balance can be negative to teach loan/credit concepts.
 * Balance is stored in cents (e.g., 100 = $1.00).
 */
@Serializable
data class Account(
    val id: String,
    val childId: String,
    val familyId: String,
    val balance: Long,  // Balance in cents, can be negative for loan/credit teaching
    val isActive: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long
)
