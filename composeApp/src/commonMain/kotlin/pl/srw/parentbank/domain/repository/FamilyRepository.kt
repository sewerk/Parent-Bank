package pl.srw.parentbank.domain.repository

import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.util.Outcome
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for family operations.
 */
interface FamilyRepository {
    suspend fun createFamily(family: Family): Outcome<Family>
    suspend fun getFamilyById(id: String): Outcome<Family>
    suspend fun getFamilyByCode(code: String): Outcome<Family>
    suspend fun updateFamily(family: Family): Outcome<Family>
    suspend fun deleteFamily(id: String): Outcome<Unit>
    fun observeFamily(id: String): Flow<Outcome<Family>>
}
