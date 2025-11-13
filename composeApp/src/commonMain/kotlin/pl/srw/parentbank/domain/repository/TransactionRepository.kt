package pl.srw.parentbank.domain.repository

import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.util.Outcome
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for transaction operations.
 */
interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Outcome<Transaction>
    suspend fun getTransactionById(id: String): Outcome<Transaction>
    suspend fun getTransactionsByAccountId(accountId: String): Outcome<List<Transaction>>
    suspend fun getPendingTransactionsByFamilyId(familyId: String): Outcome<List<Transaction>>
    suspend fun updateTransaction(transaction: Transaction): Outcome<Transaction>
    suspend fun updateTransactionStatus(
        id: String,
        status: TransactionStatus,
        processedBy: String
    ): Outcome<Transaction>
    fun observeAccountTransactions(accountId: String): Flow<Outcome<List<Transaction>>>
    fun observePendingTransactions(familyId: String): Flow<Outcome<List<Transaction>>>
}
