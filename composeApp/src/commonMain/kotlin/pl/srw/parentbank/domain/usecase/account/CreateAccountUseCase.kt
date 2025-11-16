package pl.srw.parentbank.domain.usecase.account

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Account
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome

/**
 * Use case for creating a new account for a child.
 * Accounts start with zero balance by default.
 */
class CreateAccountUseCase(
    private val accountRepository: AccountRepository
) : UseCaseWithParams<CreateAccountUseCase.Params, Account>() {

    override suspend fun execute(params: Params): Outcome<Account> {
        // Validate input
        if (params.childId.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("childId", "Child ID cannot be empty")
            )
        }
        if (params.familyId.isBlank()) {
            return Outcome.Failure(
                AppException.ValidationError("familyId", "Family ID cannot be empty")
            )
        }

        val currentTime = Clock.System.now().toEpochMilliseconds()
        val account = Account(
            id = params.id,
            childId = params.childId,
            familyId = params.familyId,
            balance = params.initialBalance,
            isActive = true,
            createdAt = currentTime,
            updatedAt = currentTime
        )

        return accountRepository.createAccount(account)
    }

    data class Params(
        val id: String,
        val childId: String,
        val familyId: String,
        val initialBalance: Long = 0L
    )
}
