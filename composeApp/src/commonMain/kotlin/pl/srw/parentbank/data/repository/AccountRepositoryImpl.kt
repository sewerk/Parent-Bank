package pl.srw.parentbank.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.Account
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of AccountRepository.
 * Supports negative balances for loan/credit teaching.
 */
class AccountRepositoryImpl(
    private val database: ParentBankDatabase
) : AccountRepository {

    private val queries = database.accountQueries

    override suspend fun createAccount(account: Account): Outcome<Account> = withContext(Dispatchers.Default) {
        try {
            queries.insert(
                id = account.id,
                childId = account.childId,
                familyId = account.familyId,
                balance = account.balance,
                isActive = if (account.isActive) 1 else 0,
                createdAt = account.createdAt,
                updatedAt = account.updatedAt
            )
            Outcome.Success(account)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to create account: ${e.message}", e)
            )
        }
    }

    override suspend fun getAccountById(id: String): Outcome<Account> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Account not found with id: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get account: ${e.message}", e)
            )
        }
    }

    override suspend fun getAccountByChildId(childId: String): Outcome<Account> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectByChildId(childId).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Account not found for child: $childId")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get account by child: ${e.message}", e)
            )
        }
    }

    override suspend fun getAccountsByFamilyId(familyId: String): Outcome<List<Account>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectByFamilyId(familyId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get accounts by family: ${e.message}", e)
            )
        }
    }

    override suspend fun updateAccount(account: Account): Outcome<Account> = withContext(Dispatchers.Default) {
        try {
            queries.update(
                balance = account.balance,
                isActive = if (account.isActive) 1 else 0,
                updatedAt = account.updatedAt,
                id = account.id
            )
            Outcome.Success(account)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update account: ${e.message}", e)
            )
        }
    }

    override suspend fun updateBalance(accountId: String, newBalance: Long): Outcome<Account> = withContext(Dispatchers.Default) {
        try {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.updateBalance(
                balance = newBalance,
                updatedAt = currentTime,
                id = accountId
            )
            // Fetch updated account
            val entity = queries.selectById(accountId).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Account not found after balance update: $accountId")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update balance: ${e.message}", e)
            )
        }
    }

    override suspend fun deactivateAccount(id: String): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.deactivate(
                updatedAt = currentTime,
                id = id
            )
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to deactivate account: ${e.message}", e)
            )
        }
    }

    override fun observeAccount(id: String): Flow<Outcome<Account>> {
        return queries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity ->
                if (entity != null) {
                    Outcome.Success(entity.toDomainModel())
                } else {
                    Outcome.Failure(
                        AppException.DatabaseError("Account not found with id: $id")
                    )
                }
            }
    }

    override fun observeFamilyAccounts(familyId: String): Flow<Outcome<List<Account>>> {
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
private fun pl.srw.parentbank.db.AccountEntity.toDomainModel(): Account {
    return Account(
        id = id,
        childId = childId,
        familyId = familyId,
        balance = balance,
        isActive = isActive == 1L,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
