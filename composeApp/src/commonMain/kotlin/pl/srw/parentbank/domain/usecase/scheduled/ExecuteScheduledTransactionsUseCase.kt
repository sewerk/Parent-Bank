package pl.srw.parentbank.domain.usecase.scheduled

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Frequency
import pl.srw.parentbank.domain.model.ScheduledTransaction
import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.domain.repository.ScheduledTransactionRepository
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.domain.usecase.UseCase
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap
import kotlin.random.Random

/**
 * Use case for executing due scheduled transactions.
 * This should be run periodically (e.g., daily) to process all due scheduled transactions.
 * Handles both fixed amounts and percentage-based calculations (e.g., compound interest).
 */
class ExecuteScheduledTransactionsUseCase(
    private val scheduledTransactionRepository: ScheduledTransactionRepository,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) : UseCase<ExecutedTransactionsResult>() {

    override suspend fun execute(): Outcome<ExecutedTransactionsResult> {
        val currentTime = Clock.System.now().toEpochMilliseconds()

        // Get all due scheduled transactions
        return scheduledTransactionRepository.getDueScheduledTransactions(currentTime).flatMap { dueTransactions ->
            val results = mutableListOf<TransactionExecutionResult>()

            // Execute each scheduled transaction
            for (scheduledTx in dueTransactions) {
                val result = executeScheduledTransaction(scheduledTx, currentTime)
                results.add(result)
            }

            Outcome.Success(
                ExecutedTransactionsResult(
                    totalProcessed = results.size,
                    successful = results.count { it.success },
                    failed = results.count { !it.success },
                    results = results
                )
            )
        }
    }

    private suspend fun executeScheduledTransaction(
        scheduledTx: ScheduledTransaction,
        currentTime: Long
    ): TransactionExecutionResult {
        // Check if end date has passed
        if (scheduledTx.endDate != null && currentTime > scheduledTx.endDate) {
            // Delete expired scheduled transaction
            scheduledTransactionRepository.deleteScheduledTransaction(scheduledTx.id)
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Scheduled transaction expired and was removed"
            )
        }

        // Get current account balance for percentage calculations
        val accountResult = accountRepository.getAccountById(scheduledTx.accountId)
        if (accountResult is Outcome.Failure) {
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Failed to get account: ${accountResult.exception.message}"
            )
        }
        val account = (accountResult as Outcome.Success).data

        // Calculate transaction amount
        val amount = try {
            if (scheduledTx.isPercentageBased()) {
                scheduledTx.calculateAmount(account.balance)
            } else {
                scheduledTx.calculateAmount()
            }
        } catch (e: Exception) {
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Failed to calculate amount: ${e.message}"
            )
        }

        // Skip if percentage-based amount is 0 (e.g., interest on 0 balance)
        if (amount == 0L) {
            // Still update next execution time
            val nextExecution = calculateNextExecution(currentTime, scheduledTx.frequency)
            scheduledTransactionRepository.updateScheduledTransaction(
                scheduledTx.copy(
                    lastExecuted = currentTime,
                    nextExecution = nextExecution,
                    updatedAt = currentTime
                )
            )
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = true,
                transactionCreated = false,
                note = "Skipped: amount is 0"
            )
        }

        // Create the transaction
        val transaction = Transaction(
            id = generateTransactionId(),
            accountId = scheduledTx.accountId,
            familyId = scheduledTx.familyId,
            type = scheduledTx.type,
            amount = amount,
            title = scheduledTx.title,
            category = scheduledTx.category,
            notes = "Auto-generated from scheduled transaction",
            status = TransactionStatus.APPROVED, // Scheduled transactions are auto-approved
            initiatedBy = scheduledTx.createdBy,
            processedBy = scheduledTx.createdBy,
            date = currentTime,
            createdAt = currentTime,
            updatedAt = currentTime
        )

        // Save transaction
        val txResult = transactionRepository.createTransaction(transaction)
        if (txResult is Outcome.Failure) {
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Failed to create transaction: ${txResult.exception.message}"
            )
        }

        // Update account balance
        val newBalance = account.balance + transaction.getEffectiveAmount()
        val balanceResult = accountRepository.updateBalance(account.id, newBalance)
        if (balanceResult is Outcome.Failure) {
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Failed to update balance: ${balanceResult.exception.message}"
            )
        }

        // Update scheduled transaction execution times
        val nextExecution = calculateNextExecution(currentTime, scheduledTx.frequency)
        val updateResult = scheduledTransactionRepository.updateScheduledTransaction(
            scheduledTx.copy(
                lastExecuted = currentTime,
                nextExecution = nextExecution,
                updatedAt = currentTime
            )
        )

        if (updateResult is Outcome.Failure) {
            return TransactionExecutionResult(
                scheduledTransactionId = scheduledTx.id,
                success = false,
                error = "Failed to update scheduled transaction: ${updateResult.exception.message}"
            )
        }

        return TransactionExecutionResult(
            scheduledTransactionId = scheduledTx.id,
            success = true,
            transactionCreated = true,
            transactionId = transaction.id,
            amount = amount
        )
    }

    /**
     * Calculates the next execution time based on current time and frequency.
     */
    private fun calculateNextExecution(currentTime: Long, frequency: Frequency): Long {
        val millisecondsInDay = 24L * 60 * 60 * 1000
        return when (frequency) {
            Frequency.DAILY -> currentTime + millisecondsInDay
            Frequency.WEEKLY -> currentTime + (7 * millisecondsInDay)
            Frequency.BIWEEKLY -> currentTime + (14 * millisecondsInDay)
            Frequency.MONTHLY -> currentTime + (30 * millisecondsInDay) // Approximate
        }
    }

    private fun generateTransactionId(): String {
        return "tx_${Clock.System.now().toEpochMilliseconds()}_${Random.nextInt(10000)}"
    }
}

/**
 * Result of executing all scheduled transactions.
 */
data class ExecutedTransactionsResult(
    val totalProcessed: Int,
    val successful: Int,
    val failed: Int,
    val results: List<TransactionExecutionResult>
)

/**
 * Result of executing a single scheduled transaction.
 */
data class TransactionExecutionResult(
    val scheduledTransactionId: String,
    val success: Boolean,
    val transactionCreated: Boolean = false,
    val transactionId: String? = null,
    val amount: Long? = null,
    val error: String? = null,
    val note: String? = null
)
