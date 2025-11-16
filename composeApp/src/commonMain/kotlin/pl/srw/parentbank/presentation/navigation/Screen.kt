package pl.srw.parentbank.presentation.navigation

/**
 * Sealed class representing all screens in the app.
 */
sealed class Screen {
    data object Welcome : Screen()
    data object CreateFamily : Screen()
    data object JoinFamily : Screen()
    data class CreateUser(val familyId: String) : Screen()
    data class ParentDashboard(val userId: String, val familyId: String) : Screen()
    data class ChildDashboard(val userId: String, val familyId: String) : Screen()
    data class CreateTransaction(val accountId: String, val userId: String) : Screen()
    data class PendingTransactions(val familyId: String, val userId: String) : Screen()
}
