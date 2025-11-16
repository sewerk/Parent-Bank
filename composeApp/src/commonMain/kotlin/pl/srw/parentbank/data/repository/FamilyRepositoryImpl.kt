package pl.srw.parentbank.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.repository.FamilyRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of FamilyRepository.
 */
class FamilyRepositoryImpl(
    private val database: ParentBankDatabase
) : FamilyRepository {

    private val queries = database.familyQueries

    override suspend fun createFamily(family: Family): Outcome<Family> = withContext(Dispatchers.Default) {
        try {
            queries.insert(
                id = family.id,
                name = family.name,
                familyCode = family.familyCode,
                currency = family.currency,
                createdAt = family.createdAt,
                updatedAt = family.updatedAt
            )
            Outcome.Success(family)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to create family: ${e.message}", e)
            )
        }
    }

    override suspend fun getFamilyById(id: String): Outcome<Family> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Family not found with id: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get family: ${e.message}", e)
            )
        }
    }

    override suspend fun getFamilyByCode(code: String): Outcome<Family> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectByCode(code).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Family not found with code: $code")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get family by code: ${e.message}", e)
            )
        }
    }

    override suspend fun updateFamily(family: Family): Outcome<Family> = withContext(Dispatchers.Default) {
        try {
            queries.update(
                name = family.name,
                currency = family.currency,
                updatedAt = family.updatedAt,
                id = family.id
            )
            Outcome.Success(family)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update family: ${e.message}", e)
            )
        }
    }

    override suspend fun deleteFamily(id: String): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            queries.deleteById(id)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to delete family: ${e.message}", e)
            )
        }
    }

    override fun observeFamily(id: String): Flow<Outcome<Family>> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                if (entity != null) {
                    Outcome.Success(entity.toDomainModel())
                } else {
                    Outcome.Failure(
                        AppException.DatabaseError("Family not found with id: $id")
                    )
                }
            }
    }
}

/**
 * Extension function to convert SQLDelight entity to domain model.
 */
private fun pl.srw.parentbank.db.FamilyEntity.toDomainModel(): Family {
    return Family(
        id = id,
        name = name,
        familyCode = familyCode,
        currency = currency,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
