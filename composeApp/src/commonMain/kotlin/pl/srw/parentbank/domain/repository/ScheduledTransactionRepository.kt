package pl.srw.parentbank.domain.repository

import pl.srw.parentbank.domain.model.ScheduledTransaction
import pl.srw.parentbank.util.Outcome
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for scheduled transaction operations.
 */
interface ScheduledTransactionRepository {
    suspend fun createScheduledTransaction(scheduledTransaction: ScheduledTransaction): Outcome<ScheduledTransaction>
    suspend fun getScheduledTransactionById(id: String): Outcome<ScheduledTransaction>
    suspend fun getScheduledTransactionsByAccountId(accountId: String): Outcome<List<ScheduledTransaction>>
    suspend fun getScheduledTransactionsByFamilyId(familyId: String): Outcome<List<ScheduledTransaction>>
    suspend fun getDueScheduledTransactions(currentTime: Long): Outcome<List<ScheduledTransaction>>
    suspend fun updateScheduledTransaction(scheduledTransaction: ScheduledTransaction): Outcome<ScheduledTransaction>
    suspend fun pauseScheduledTransaction(id: String): Outcome<ScheduledTransaction>
    suspend fun resumeScheduledTransaction(id: String): Outcome<ScheduledTransaction>
    suspend fun deleteScheduledTransaction(id: String): Outcome<Unit>
    fun observeAccountScheduledTransactions(accountId: String): Flow<Outcome<List<ScheduledTransaction>>>
}
