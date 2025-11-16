package pl.srw.parentbank.data.repository

import kotlinx.datetime.Clock
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of TransactionRepository.
 */
class TransactionRepositoryImpl(
    private val database: ParentBankDatabase
) : TransactionRepository {

    private val queries = database.transactionQueries

    override suspend fun createTransaction(transaction: Transaction): Outcome<Transaction> = withContext(Dispatchers.Default) {
        try {
            queries.insert(
                id = transaction.id,
                accountId = transaction.accountId,
                familyId = transaction.familyId,
                type = transaction.type.name,
                amount = transaction.amount,
                title = transaction.title,
                category = transaction.category,
                notes = transaction.notes,
                status = transaction.status.name,
                initiatedBy = transaction.initiatedBy,
                processedBy = transaction.processedBy,
                date = transaction.date,
                createdAt = transaction.createdAt,
                updatedAt = transaction.updatedAt
            )
            Outcome.Success(transaction)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to create transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun getTransactionById(id: String): Outcome<Transaction> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Transaction not found with id: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun getTransactionsByAccountId(accountId: String): Outcome<List<Transaction>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectByAccountId(accountId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get transactions by account: ${e.message}", e)
            )
        }
    }

    override suspend fun getPendingTransactionsByFamilyId(familyId: String): Outcome<List<Transaction>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectPendingByFamilyId(familyId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get pending transactions: ${e.message}", e)
            )
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Outcome<Transaction> = withContext(Dispatchers.Default) {
        try {
            queries.update(
                type = transaction.type.name,
                amount = transaction.amount,
                title = transaction.title,
                category = transaction.category,
                notes = transaction.notes,
                status = transaction.status.name,
                updatedAt = transaction.updatedAt,
                id = transaction.id
            )
            Outcome.Success(transaction)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun updateTransactionStatus(
        id: String,
        status: TransactionStatus,
        processedBy: String
    ): Outcome<Transaction> = withContext(Dispatchers.Default) {
        try {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.updateStatus(
                status = status.name,
                processedBy = processedBy,
                updatedAt = currentTime,
                id = id
            )
            // Fetch updated transaction
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Transaction not found after status update: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update transaction status: ${e.message}", e)
            )
        }
    }

    override fun observeAccountTransactions(accountId: String): Flow<Outcome<List<Transaction>>> {
        return queries.selectByAccountId(accountId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                Outcome.Success(entities.map { it.toDomainModel() })
            }
    }

    override fun observePendingTransactions(familyId: String): Flow<Outcome<List<Transaction>>> {
        return queries.selectPendingByFamilyId(familyId)
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
private fun pl.srw.parentbank.db.TransactionEntity.toDomainModel(): Transaction {
    return Transaction(
        id = id,
        accountId = accountId,
        familyId = familyId,
        type = TransactionType.valueOf(type),
        amount = amount,
        title = title,
        category = category,
        notes = notes,
        status = TransactionStatus.valueOf(status),
        initiatedBy = initiatedBy,
        processedBy = processedBy,
        date = date,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
