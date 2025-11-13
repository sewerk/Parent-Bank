package pl.srw.parentbank.domain.model

/**
 * Enum representing transaction status.
 */
enum class TransactionStatus {
    PENDING,   // Waiting for parent approval (child-initiated only)
    APPROVED,  // Approved and processed
    DENIED     // Denied by parent
}
