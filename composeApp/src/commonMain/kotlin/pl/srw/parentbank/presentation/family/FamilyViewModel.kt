package pl.srw.parentbank.presentation.family

import kotlinx.datetime.Clock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.Family
import pl.srw.parentbank.domain.usecase.family.CreateFamilyUseCase
import pl.srw.parentbank.domain.usecase.family.JoinFamilyUseCase
import pl.srw.parentbank.util.Outcome
import kotlin.random.Random

/**
 * ViewModel for family creation and joining.
 */
class FamilyViewModel(
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val joinFamilyUseCase: JoinFamilyUseCase
) : ViewModel() {

    var state by mutableStateOf(FamilyState())
        private set

    fun onFamilyNameChanged(name: String) {
        state = state.copy(familyName = name, error = null)
    }

    fun onCurrencyChanged(currency: String) {
        state = state.copy(currency = currency, error = null)
    }

    fun onFamilyCodeChanged(code: String) {
        state = state.copy(familyCode = code.uppercase(), error = null)
    }

    fun createFamily() {
        if (state.familyName.isBlank()) {
            state = state.copy(error = "Family name cannot be empty")
            return
        }
        if (state.currency.isBlank()) {
            state = state.copy(error = "Currency cannot be empty")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val params = CreateFamilyUseCase.Params(
                id = "family_${Clock.System.now().toEpochMilliseconds()}_${Random.nextInt(10000)}",
                name = state.familyName,
                currency = state.currency
            )

            when (val result = createFamilyUseCase(params)) {
                is Outcome.Success -> {
                    state = state.copy(
                        isLoading = false,
                        createdFamily = result.data,
                        error = null
                    )
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to create family"
                    )
                }
            }
        }
    }

    fun joinFamily() {
        if (state.familyCode.isBlank()) {
            state = state.copy(error = "Family code cannot be empty")
            return
        }
        if (state.familyCode.length != 6) {
            state = state.copy(error = "Family code must be 6 characters")
            return
        }

        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val params = JoinFamilyUseCase.Params(familyCode = state.familyCode)

            when (val result = joinFamilyUseCase(params)) {
                is Outcome.Success -> {
                    state = state.copy(
                        isLoading = false,
                        joinedFamily = result.data,
                        error = null
                    )
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to join family"
                    )
                }
            }
        }
    }

    fun clearState() {
        state = FamilyState()
    }
}

data class FamilyState(
    val familyName: String = "",
    val currency: String = "USD",
    val familyCode: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdFamily: Family? = null,
    val joinedFamily: Family? = null
)
