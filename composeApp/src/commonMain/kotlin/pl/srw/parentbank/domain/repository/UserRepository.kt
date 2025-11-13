package pl.srw.parentbank.domain.repository

import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.util.Outcome
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user operations.
 */
interface UserRepository {
    suspend fun createUser(user: User): Outcome<User>
    suspend fun getUserById(id: String): Outcome<User>
    suspend fun getUsersByFamilyId(familyId: String): Outcome<List<User>>
    suspend fun updateUser(user: User): Outcome<User>
    suspend fun deleteUser(id: String): Outcome<Unit>
    fun observeUser(id: String): Flow<Outcome<User>>
    fun observeFamilyUsers(familyId: String): Flow<Outcome<List<User>>>
}
