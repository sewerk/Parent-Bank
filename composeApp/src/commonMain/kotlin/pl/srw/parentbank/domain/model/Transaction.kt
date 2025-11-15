package pl.srw.parentbank.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model representing a transaction.
 * Income transactions increase balance, Expense transactions decrease balance.
 * Amount is stored in cents (e.g., 100 = $1.00).
 */
@Serializable
data class Transaction(
    val id: String,
    val accountId: String,
    val familyId: String,
    val type: TransactionType,  // INCOME or EXPENSE
    val amount: Long,  // Amount in cents, always positive, type determines if it adds or subtracts
    val title: String,
    val category: String? = null,
    val notes: String? = null,
    val status: TransactionStatus,  // PENDING, APPROVED, DENIED
    val initiatedBy: String,  // User ID who initiated the transaction
    val processedBy: String? = null,  // User ID who approved/denied (for child requests)
    val date: Long,  // Transaction date
    val createdAt: Long,
    val updatedAt: Long
) {
    init {
        // Fail-fast validation: amount must be positive
        require(amount > 0) { "Transaction amount must be positive. Got: $amount" }
    }

    /**
     * Returns the effective amount based on transaction type.
     * Income: positive, Expense: negative
     */
    fun getEffectiveAmount(): Long {
        return when (type) {
            TransactionType.INCOME -> amount
            TransactionType.EXPENSE -> -amount
        }
    }
}
