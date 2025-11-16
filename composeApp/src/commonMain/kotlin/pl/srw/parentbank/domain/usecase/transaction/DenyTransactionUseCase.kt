package pl.srw.parentbank.domain.usecase.transaction

import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionStatus
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap

/**
 * Use case for denying a pending transaction.
 * Only parents can deny transactions.
 * Denying does NOT update the account balance.
 */
class DenyTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : UseCaseWithParams<DenyTransactionUseCase.Params, Transaction>() {

    override suspend fun execute(params: Params): Outcome<Transaction> {
        // Validate the user is a parent
        return userRepository.getUserById(params.deniedBy).flatMap { user ->
            if (user.role != UserRole.PARENT) {
                return@flatMap Outcome.Failure(
                    AppException.BusinessError("Only parents can deny transactions")
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
                        AppException.BusinessError("User not authorized to deny this transaction")
                    )
                }

                // Update transaction status to DENIED
                transactionRepository.updateTransactionStatus(
                    id = transaction.id,
                    status = TransactionStatus.DENIED,
                    processedBy = params.deniedBy
                )
            }
        }
    }

    data class Params(
        val transactionId: String,
        val deniedBy: String
    )
}
