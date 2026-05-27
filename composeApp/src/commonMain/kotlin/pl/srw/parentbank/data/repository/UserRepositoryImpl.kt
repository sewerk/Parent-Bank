package pl.srw.parentbank.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.db.UserEntity
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

class UserRepositoryImpl(
    private val database: ParentBankDatabase
) : UserRepository {

    private val queries get() = database.userQueries

    override suspend fun createUser(user: User): Outcome<User> = withContext(Dispatchers.IO) {
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
            Outcome.Failure(AppException.DatabaseError("Failed to create user", e))
        }
    }

    override suspend fun getUserById(id: String): Outcome<User> = withContext(Dispatchers.IO) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomain())
            } else {
                Outcome.Failure(AppException.BusinessError("User not found"))
            }
        } catch (e: Exception) {
            Outcome.Failure(AppException.DatabaseError("Failed to get user", e))
        }
    }

    override suspend fun getUsersByFamilyId(familyId: String): Outcome<List<User>> = withContext(Dispatchers.IO) {
        try {
            val entities = queries.selectByFamilyId(familyId).executeAsList()
            Outcome.Success(entities.map { it.toDomain() })
        } catch (e: Exception) {
            Outcome.Failure(AppException.DatabaseError("Failed to get users", e))
        }
    }

    override suspend fun updateUser(user: User): Outcome<User> = withContext(Dispatchers.IO) {
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
            Outcome.Failure(AppException.DatabaseError("Failed to update user", e))
        }
    }

    override suspend fun deleteUser(id: String): Outcome<Unit> = withContext(Dispatchers.IO) {
        try {
            queries.deleteById(id)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(AppException.DatabaseError("Failed to delete user", e))
        }
    }

    override fun observeUser(id: String): Flow<Outcome<User>> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity ->
                if (entity != null) {
                    Outcome.Success(entity.toDomain())
                } else {
                    Outcome.Failure(AppException.BusinessError("User not found"))
                }
            }
    }

    override fun observeFamilyUsers(familyId: String): Flow<Outcome<List<User>>> {
        return queries.selectByFamilyId(familyId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                Outcome.Success(entities.map { it.toDomain() })
            }
    }
}

private fun UserEntity.toDomain() = User(
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
