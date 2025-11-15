package pl.srw.parentbank.domain.usecase.transaction

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap

/**
 * Use case for creating a new transaction.
 * - Parents can create APPROVED transactions directly
 * - Children can create PENDING transactions that require approval
 */
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : UseCaseWithParams<CreateTransactionUseCase.Params, Transaction>() {

    override suspend fun execute(params: Params): Outcome<Transaction> {
        // Validate input
        if (params.accountId.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("accountId", "Account ID cannot be empty")
            )
        }
        if (params.title.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("title", "Title cannot be empty")
            )
        }
        if (params.amount <= 0) {
            return Outcome.Failure(
                AppException.ValidationError("amount", "Amount must be positive")
            )
        }

        // Get the user who is initiating the transaction
        return userRepository.getUserById(params.initiatedBy).flatMap { user ->
            // Determine transaction status based on user role
            val status = when (user.role) {
                UserRole.PARENT -> TransactionStatus.APPROVED
                UserRole.CHILD -> TransactionStatus.PENDING
            }

            val currentTime = Clock.System.now().toEpochMilliseconds()
            val transaction = Transaction(
                id = params.id,
                accountId = params.accountId,
                familyId = user.familyId,
                type = params.type,
                amount = params.amount,
                title = params.title,
                category = params.category,
                notes = params.notes,
                status = status,
                initiatedBy = params.initiatedBy,
                processedBy = if (status == TransactionStatus.APPROVED) params.initiatedBy else null,
                date = params.date ?: currentTime,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            // Create transaction
            transactionRepository.createTransaction(transaction).flatMap { createdTransaction ->
                // If parent created it (auto-approved), update account balance
                if (status == TransactionStatus.APPROVED) {
                    updateAccountBalance(createdTransaction)
                } else {
                    // Child transaction - no balance update until approved
                    Outcome.Success(createdTransaction)
                }
            }
        }
    }

    private suspend fun updateAccountBalance(transaction: Transaction): Outcome<Transaction> {
        return accountRepository.getAccountById(transaction.accountId).flatMap { account ->
            val newBalance = account.balance + transaction.getEffectiveAmount()
            accountRepository.updateBalance(account.id, newBalance).flatMap {
                Outcome.Success(transaction)
            }
        }
    }

    data class Params(
        val id: String,
        val accountId: String,
        val type: TransactionType,
        val amount: Long,
        val title: String,
        val category: String? = null,
        val notes: String? = null,
        val initiatedBy: String,
        val date: Long? = null
    )
}
