package pl.srw.parentbank.data.repository
import kotlinx.datetime.Clock

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.srw.parentbank.db.ParentBankDatabase
import pl.srw.parentbank.domain.model.Frequency
import pl.srw.parentbank.domain.model.ScheduledTransaction
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.repository.ScheduledTransactionRepository
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * SQLDelight implementation of ScheduledTransactionRepository.
 * Supports both fixed amounts and percentage-based calculations (e.g., interest).
 */
class ScheduledTransactionRepositoryImpl(
    private val database: ParentBankDatabase
) : ScheduledTransactionRepository {

    private val queries = database.scheduledTransactionQueries

    override suspend fun createScheduledTransaction(scheduledTransaction: ScheduledTransaction): Outcome<ScheduledTransaction> = withContext(Dispatchers.Default) {
        try {
            queries.insert(
                id = scheduledTransaction.id,
                accountId = scheduledTransaction.accountId,
                familyId = scheduledTransaction.familyId,
                type = scheduledTransaction.type.name,
                title = scheduledTransaction.title,
                category = scheduledTransaction.category,
                frequency = scheduledTransaction.frequency.name,
                fixedAmount = scheduledTransaction.fixedAmount,
                percentageRate = scheduledTransaction.percentageRate,
                startDate = scheduledTransaction.startDate,
                endDate = scheduledTransaction.endDate,
                lastExecuted = scheduledTransaction.lastExecuted,
                nextExecution = scheduledTransaction.nextExecution,
                isPaused = if (scheduledTransaction.isPaused) 1 else 0,
                createdBy = scheduledTransaction.createdBy,
                createdAt = scheduledTransaction.createdAt,
                updatedAt = scheduledTransaction.updatedAt
            )
            Outcome.Success(scheduledTransaction)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to create scheduled transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun getScheduledTransactionById(id: String): Outcome<ScheduledTransaction> = withContext(Dispatchers.Default) {
        try {
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Scheduled transaction not found with id: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get scheduled transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun getScheduledTransactionsByAccountId(accountId: String): Outcome<List<ScheduledTransaction>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectByAccountId(accountId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get scheduled transactions by account: ${e.message}", e)
            )
        }
    }

    override suspend fun getScheduledTransactionsByFamilyId(familyId: String): Outcome<List<ScheduledTransaction>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectByFamilyId(familyId).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get scheduled transactions by family: ${e.message}", e)
            )
        }
    }

    override suspend fun getDueScheduledTransactions(currentTime: Long): Outcome<List<ScheduledTransaction>> = withContext(Dispatchers.Default) {
        try {
            val entities = queries.selectDue(currentTime).executeAsList()
            Outcome.Success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to get due scheduled transactions: ${e.message}", e)
            )
        }
    }

    override suspend fun updateScheduledTransaction(scheduledTransaction: ScheduledTransaction): Outcome<ScheduledTransaction> = withContext(Dispatchers.Default) {
        try {
            queries.update(
                title = scheduledTransaction.title,
                category = scheduledTransaction.category,
                frequency = scheduledTransaction.frequency.name,
                fixedAmount = scheduledTransaction.fixedAmount,
                percentageRate = scheduledTransaction.percentageRate,
                endDate = scheduledTransaction.endDate,
                updatedAt = scheduledTransaction.updatedAt,
                id = scheduledTransaction.id
            )

            // Also update execution times if they changed
            queries.updateExecution(
                lastExecuted = scheduledTransaction.lastExecuted,
                nextExecution = scheduledTransaction.nextExecution,
                updatedAt = scheduledTransaction.updatedAt,
                id = scheduledTransaction.id
            )

            Outcome.Success(scheduledTransaction)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to update scheduled transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun pauseScheduledTransaction(id: String): Outcome<ScheduledTransaction> = withContext(Dispatchers.Default) {
        try {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.pause(
                updatedAt = currentTime,
                id = id
            )
            // Fetch updated scheduled transaction
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Scheduled transaction not found after pause: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to pause scheduled transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun resumeScheduledTransaction(id: String): Outcome<ScheduledTransaction> = withContext(Dispatchers.Default) {
        try {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            queries.resume(
                updatedAt = currentTime,
                id = id
            )
            // Fetch updated scheduled transaction
            val entity = queries.selectById(id).executeAsOneOrNull()
            if (entity != null) {
                Outcome.Success(entity.toDomainModel())
            } else {
                Outcome.Failure(
                    AppException.DatabaseError("Scheduled transaction not found after resume: $id")
                )
            }
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to resume scheduled transaction: ${e.message}", e)
            )
        }
    }

    override suspend fun deleteScheduledTransaction(id: String): Outcome<Unit> = withContext(Dispatchers.Default) {
        try {
            queries.deleteById(id)
            Outcome.Success(Unit)
        } catch (e: Exception) {
            Outcome.Failure(
                AppException.DatabaseError("Failed to delete scheduled transaction: ${e.message}", e)
            )
        }
    }

    override fun observeAccountScheduledTransactions(accountId: String): Flow<Outcome<List<ScheduledTransaction>>> {
        return queries.selectByAccountId(accountId)
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
private fun pl.srw.parentbank.db.ScheduledTransactionEntity.toDomainModel(): ScheduledTransaction {
    return ScheduledTransaction(
        id = id,
        accountId = accountId,
        familyId = familyId,
        type = TransactionType.valueOf(type),
        title = title,
        category = category,
        frequency = Frequency.valueOf(frequency),
        fixedAmount = fixedAmount,
        percentageRate = percentageRate,
        startDate = startDate,
        endDate = endDate,
        lastExecuted = lastExecuted,
        nextExecution = nextExecution,
        isPaused = isPaused == 1L,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
