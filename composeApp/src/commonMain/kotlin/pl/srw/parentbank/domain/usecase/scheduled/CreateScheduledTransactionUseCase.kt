package pl.srw.parentbank.domain.usecase.scheduled

import kotlinx.datetime.Clock
import pl.srw.parentbank.domain.model.Frequency
import pl.srw.parentbank.domain.model.ScheduledTransaction
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.repository.ScheduledTransactionRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.domain.usecase.UseCaseWithParams
import pl.srw.parentbank.util.AppException
import pl.srw.parentbank.util.Outcome
import pl.srw.parentbank.util.flatMap

/**
 * Use case for creating a scheduled recurring transaction.
 * Supports both fixed amounts and percentage-based calculations (e.g., interest).
 */
class CreateScheduledTransactionUseCase(
    private val scheduledTransactionRepository: ScheduledTransactionRepository,
    private val userRepository: UserRepository
) : UseCaseWithParams<CreateScheduledTransactionUseCase.Params, ScheduledTransaction>() {

    override suspend fun execute(params: Params): Outcome<ScheduledTransaction> {
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

        // Validate either fixed amount or percentage rate is provided
        if ((params.fixedAmount == null) == (params.percentageRate == null)) {
            return Outcome.Failure(
                AppException.ValidationError(
                    "amount",
                    "Must provide either fixed amount or percentage rate, not both"
                )
            )
        }

        // Validate positive values
        params.fixedAmount?.let {
            if (it <= 0) {
                return Outcome.Failure(
                    AppException.ValidationError("fixedAmount", "Fixed amount must be positive")
                )
            }
        }
        params.percentageRate?.let {
            if (it <= 0) {
                return Outcome.Failure(
                    AppException.ValidationError("percentageRate", "Percentage rate must be positive")
                )
            }
        }

        // Get the user creating the schedule
        return userRepository.getUserById(params.createdBy).flatMap { user ->
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val nextExecution = calculateNextExecution(params.startDate, params.frequency)

            val scheduledTransaction = ScheduledTransaction(
                id = params.id,
                accountId = params.accountId,
                familyId = user.familyId,
                type = params.type,
                title = params.title,
                category = params.category,
                frequency = params.frequency,
                fixedAmount = params.fixedAmount,
                percentageRate = params.percentageRate,
                startDate = params.startDate,
                endDate = params.endDate,
                lastExecuted = null,
                nextExecution = nextExecution,
                isPaused = false,
                createdBy = params.createdBy,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            scheduledTransactionRepository.createScheduledTransaction(scheduledTransaction)
        }
    }

    /**
     * Calculates the next execution time based on frequency.
     */
    private fun calculateNextExecution(startDate: Long, frequency: Frequency): Long {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        return if (startDate > currentTime) {
            startDate
        } else {
            currentTime
        }
    }

    data class Params(
        val id: String,
        val accountId: String,
        val type: TransactionType,
        val title: String,
        val category: String? = null,
        val frequency: Frequency,
        val fixedAmount: Long? = null,
        val percentageRate: Double? = null,
        val startDate: Long,
        val endDate: Long? = null,
        val createdBy: String
    )
}
