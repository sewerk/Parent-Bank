package pl.srw.parentbank.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of UserRepository.
 */
class UserRepositoryImpl(
    private val database: ParentBankDatabase
) : UserRepository {

    private val queries = database.userQueries

    override suspend fun createUser(user: User): Outcome<User> = withContext(Dispatchers.Default) {
        try {
            queries.insert(
                id = user.id,
                familyId = user.familyId,
                name = user.name,
                email = user.email,
                role = user.role.name,
                age = user.age?.toLong(),
                profilePictureUrl = user.profilePictureUrl,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
            Outcome.Success(user)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to create user: ${e.message}", e)
            )
        }
    }

    override suspend fun getUserById(id: String): Outcome<User> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("User not found with id: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get user: ${e.message}", e)
            )
        }
    }

    override suspend fun getUsersByFamilyId(familyId: String): Outcome<List<User>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectByFamilyId(familyId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get users by family: ${e.message}", e)
            )
        }
    }

    override suspend fun updateUser(user: User): Outcome<User> = withContext(Dispatchers.Default) {
        try {
            queries.update(
                name = user.name,
                email = user.email,
                age = user.age?.toLong(),
                profilePictureUrl = user.profilePictureUrl,
                updatedAt = user.updatedAt,
                id = user.id
            )
            Outcome.Success(user)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update user: ${e.message}", e)
            )
        }
    }

    override suspend fun deleteUser(id: String): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            queries.deleteById(id)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to delete user: ${e.message}", e)
            )
        }
    }

    override fun observeUser(id: String): Flow<Outcome<User>> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                if (entity != null) {
                    Outcome.Success(entity.toDomainModel())
                } else {
                    Outcome.Failure(
                        AppException.DatabaseError("User not found with id: $id")
                    )
                }
            }
    }

    override fun observeFamilyUsers(familyId: String): Flow<Outcome<List<User>>> {
        return queries.selectByFamilyId(familyId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                Outcome.Success(entities.map { it.toDomainModel() })
            }
    }
}

/**
 * Extension function to convert SQLDelight entity to domain model.
 */
private fun pl.srw.parentbank.db.UserEntity.toDomainModel(): User {
    return User(
        id = id,
        familyId = familyId,
        name = name,
        email = email,
        role = UserRole.valueOf(role),
        age = age?.toInt(),
        profilePictureUrl = profilePictureUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
