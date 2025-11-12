package pl.srw.parentbank.domain.model

/**
 * Enum representing user roles in the family.
 */
enum class UserRole {
    PARENT,  // Full permissions, can manage all accounts
    CHILD    // Limited permissions, requires parent approval
}
