package pl.srw.parentbank.presentation.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.Account
import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.repository.AccountRepository
import pl.srw.parentbank.domain.repository.TransactionRepository
import pl.srw.parentbank.domain.repository.UserRepository
import pl.srw.parentbank.util.Outcome

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun loadDashboard(userId: String, familyId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Load user
            when (val userResult = userRepository.getUserById(userId)) {
                is Outcome.Success -> {
                    _state.value = _state.value.copy(currentUser = userResult.data)

                    // Load accounts based on user role
                    loadAccountsForUser(userResult.data, familyId)
                }
                is Outcome.Failure -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = userResult.exception.message
                    )
                }
            }
        }
    }

    private suspend fun loadAccountsForUser(user: User, familyId: String) {
        when (val accountsResult = accountRepository.getAccountsByFamilyId(familyId)) {
            is Outcome.Success -> {
                val accounts = accountsResult.data
                val userAccount = accounts.firstOrNull { it.childId == user.id }

                _state.value = _state.value.copy(
                    accounts = accounts,
                    userAccount = userAccount,
                    isLoading = false
                )

                // Load pending transactions if parent
                if (user.role == pl.srw.parentbank.domain.model.UserRole.PARENT) {
                    loadPendingTransactions(familyId)
                } else if (userAccount != null) {
                    loadAccountTransactions(userAccount.id)
                }
            }
            is Outcome.Failure -> {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = accountsResult.exception.message
                )
            }
        }
    }

    private suspend fun loadPendingTransactions(familyId: String) {
        when (val result = transactionRepository.getPendingTransactionsByFamilyId(familyId)) {
            is Outcome.Success -> {
                _state.value = _state.value.copy(pendingTransactions = result.data)
            }
            is Outcome.Failure -> {
                // Not critical, just log
            }
        }
    }

    private suspend fun loadAccountTransactions(accountId: String) {
        when (val result = transactionRepository.getTransactionsByAccountId(accountId)) {
            is Outcome.Success -> {
                _state.value = _state.value.copy(recentTransactions = result.data.take(10))
            }
            is Outcome.Failure -> {
                // Not critical
            }
        }
    }

    fun refresh(userId: String, familyId: String) {
        loadDashboard(userId, familyId)
    }
}

data class DashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: User? = null,
    val userAccount: Account? = null,
    val accounts: List<Account> = emptyList(),
    val pendingTransactions: List<Transaction> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
)
