package pl.srw.parentbank.presentation.transaction
import kotlinx.datetime.Clock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.Transaction
import pl.srw.parentbank.domain.model.TransactionType
import pl.srw.parentbank.domain.usecase.transaction.ApproveTransactionUseCase
import pl.srw.parentbank.domain.usecase.transaction.CreateTransactionUseCase
import pl.srw.parentbank.domain.usecase.transaction.DenyTransactionUseCase
import pl.srw.parentbank.util.Outcome
import kotlin.random.Random

class TransactionViewModel(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val approveTransactionUseCase: ApproveTransactionUseCase,
    private val denyTransactionUseCase: DenyTransactionUseCase
) : ViewModel() {

    var state by mutableStateOf(TransactionState())
        private set

    fun onTitleChanged(title: String) {
        state = state.copy(title = title, error = null)
    }

    fun onAmountChanged(amount: String) {
        val cents = (amount.toDoubleOrNull() ?: 0.0) * 100
        state = state.copy(amountCents = cents.toLong(), error = null)
    }

    fun onTypeSelected(type: TransactionType) {
        state = state.copy(type = type, error = null)
    }

    fun onCategoryChanged(category: String) {
        state = state.copy(category = category, error = null)
    }

    fun onNotesChanged(notes: String) {
        state = state.copy(notes = notes, error = null)
    }

    fun createTransaction(accountId: String, userId: String) {
        if (state.title.isBlank()) {
            state = state.copy(error = "Title is required")
            return
        }
        if (state.amountCents <= 0) {
            state = state.copy(error = "Amount must be greater than 0")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val params = CreateTransactionUseCase.Params(
                id = "tx_${Clock.System.now().toEpochMilliseconds()}_${Random.nextInt(10000)}",
                accountId = accountId,
                type = state.type,
                amount = state.amountCents,
                title = state.title,
                category = state.category.ifBlank { null },
                notes = state.notes.ifBlank { null },
                initiatedBy = userId
            )

            when (val result = createTransactionUseCase(params)) {
                is Outcome.Success -> {
                    state = state.copy(
                        isLoading = false,
                        transactionCreated = result.data
                    )
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to create transaction"
                    )
                }
            }
        }
    }

    fun approveTransaction(transactionId: String, userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val params = ApproveTransactionUseCase.Params(
                transactionId = transactionId,
                approvedBy = userId
            )

            when (val result = approveTransactionUseCase(params)) {
                is Outcome.Success -> {
                    state = state.copy(isLoading = false)
                    onSuccess()
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to approve"
                    )
                }
            }
        }
    }

    fun denyTransaction(transactionId: String, userId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val params = DenyTransactionUseCase.Params(
                transactionId = transactionId,
                deniedBy = userId
            )

            when (val result = denyTransactionUseCase(params)) {
                is Outcome.Success -> {
                    state = state.copy(isLoading = false)
                    onSuccess()
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to deny"
                    )
                }
            }
        }
    }

    fun reset() {
        state = TransactionState()
    }
}

data class TransactionState(
    val title: String = "",
    val amountCents: Long = 0,
    val type: TransactionType = TransactionType.INCOME,
    val category: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val transactionCreated: Transaction? = null
)
