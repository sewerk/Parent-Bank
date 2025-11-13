package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a family unit.
 */
@Serializable
data class Family(
    val id: String,
    val name: String,
    val familyCode: String,  // Unique code for joining the family
    val currency: String,
    val createdAt: Long,
    val updatedAt: Long
)
