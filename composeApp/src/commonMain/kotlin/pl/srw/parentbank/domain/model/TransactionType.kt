package pl.srw.parentbank.domain.model

/**
 * Enum representing transaction types.
 * Income increases balance, Expense decreases balance.
 */
enum class TransactionType {
    INCOME,   // Adds to balance (allowance, gifts, rewards)
    EXPENSE   // Subtracts from balance (purchases, withdrawals)
}
