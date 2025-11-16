package pl.srw.parentbank.domain.usecase.transaction

import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap

/**
 * Use case for approving a pending transaction.
 * Only parents can approve transactions.
 * Approving updates the account balance.
 */
class ApproveTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : UseCaseWithParams<ApproveTransactionUseCase.Params, Transaction>() {

    override suspend fun execute(params: Params): Outcome<Transaction> {
        // Validate the user is a parent
        return userRepository.getUserById(params.approvedBy).flatMap { user ->
            if (user.role != UserRole.PARENT) {
                return@flatMap Outcome.Failure(
                    AppException.BusinessError("Only parents can approve transactions")
                )
            }

            // Get the transaction
            transactionRepository.getTransactionById(params.transactionId).flatMap { transaction ->
                // Validate transaction status
                if (transaction.status != TransactionStatus.PENDING) {
                    return@flatMap Outcome.Failure(
                        AppException.BusinessError("Transaction is not pending")
                    )
                }

                // Validate family membership
                if (transaction.familyId != user.familyId) {
                    return@flatMap Outcome.Failure(
                        AppException.BusinessError("User not authorized to approve this transaction")
                    )
                }

                // Update transaction status
                transactionRepository.updateTransactionStatus(
                    id = transaction.id,
                    status = TransactionStatus.APPROVED,
                    processedBy = params.approvedBy
                ).flatMap { updatedTransaction ->
                    // Update account balance
                    updateAccountBalance(updatedTransaction)
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
        val transactionId: String,
        val approvedBy: String
    )
}
