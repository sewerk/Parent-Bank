package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a scheduled recurring transaction.
 * Supports both fixed amounts and percentage-based calculations (e.g., interest).
 * Fixed amounts are stored in cents (e.g., 100 = $1.00).
 */
@Serializable
data class ScheduledTransaction(
    val id: String,
    val accountId: String,
    val familyId: String,
    val type: TransactionType,  // INCOME or EXPENSE
    val title: String,
    val category: String? = null,
    val frequency: Frequency,
    val fixedAmount: Long? = null,  // For fixed recurring transactions (in cents)
    val percentageRate: Double? = null,  // For percentage-based (e.g., 5% monthly interest)
    val startDate: Long,
    val endDate: Long? = null,  // Optional end date
    val lastExecuted: Long? = null,  // Last execution timestamp
    val nextExecution: Long,  // Next scheduled execution
    val isPaused: Boolean = false,
    val createdBy: String,  // User ID who created the schedule
    val createdAt: Long,
    val updatedAt: Long
) {
    init {
        // Fail-fast validation: must have either fixed amount or percentage rate, not both
        require((fixedAmount != null) xor (percentageRate != null)) {
            "ScheduledTransaction must have either fixedAmount or percentageRate, but not both or neither"
        }

        // Validate positive values
        fixedAmount?.let { require(it > 0) { "Fixed amount must be positive. Got: $it" } }
        percentageRate?.let { require(it > 0) { "Percentage rate must be positive. Got: $it" } }
    }

    /**
     * Returns true if this is a percentage-based transaction (like interest).
     */
    fun isPercentageBased(): Boolean = percentageRate != null

    /**
     * Calculates the amount for the next execution.
     * For fixed amounts, returns the fixed amount.
     * For percentage-based, requires the current balance (in cents).
     * @param currentBalance The current account balance in cents
     * @return The calculated amount in cents
     */
    fun calculateAmount(currentBalance: Long? = null): Long {
        return when {
            fixedAmount != null -> fixedAmount
            percentageRate != null -> {
                requireNotNull(currentBalance) {
                    "Current balance is required for percentage-based calculations"
                }
                (currentBalance * (percentageRate / 100.0)).toLong()
            }
            else -> error("Invalid ScheduledTransaction state")
        }
    }
}
