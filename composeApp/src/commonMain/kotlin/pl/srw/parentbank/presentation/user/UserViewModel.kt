package pl.srw.parentbank.presentation.user

import kotlinx.datetime.Clock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.usecase.account.CreateAccountUseCase
import pl.srw.parentbank.domain.usecase.user.CreateUserUseCase
import pl.srw.parentbank.util.Outcome
import kotlin.random.Random

class UserViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val createAccountUseCase: CreateAccountUseCase
) : ViewModel() {

    var state by mutableStateOf(UserState())
        private set

    fun onNameChanged(name: String) {
        state = state.copy(name = name, error = null)
    }

    fun onEmailChanged(email: String) {
        state = state.copy(email = email, error = null)
    }

    fun onAgeChanged(age: String) {
        state = state.copy(age = age.toIntOrNull() ?: 0, error = null)
    }

    fun onRoleSelected(role: UserRole) {
        state = state.copy(selectedRole = role, error = null)
    }

    fun createUser(familyId: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)

            val userId = "user_${Clock.System.now().toEpochMilliseconds()}_${Random.nextInt(10000)}"
            val params = CreateUserUseCase.Params(
                id = userId,
                familyId = familyId,
                name = state.name,
                email = if (state.selectedRole == UserRole.PARENT) state.email else null,
                role = state.selectedRole,
                age = if (state.selectedRole == UserRole.CHILD) state.age else null
            )

            when (val result = createUserUseCase(params)) {
                is Outcome.Success -> {
                    // If child, create account
                    if (state.selectedRole == UserRole.CHILD) {
                        createAccountForChild(result.data, familyId)
                    } else {
                        state = state.copy(
                            isLoading = false,
                            createdUser = result.data
                        )
                    }
                }
                is Outcome.Failure -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Failed to create user"
                    )
                }
            }
        }
    }

    private suspend fun createAccountForChild(user: User, familyId: String) {
        val accountParams = CreateAccountUseCase.Params(
            id = "account_${Clock.System.now().toEpochMilliseconds()}_${Random.nextInt(10000)}",
            childId = user.id,
            familyId = familyId
        )

        when (createAccountUseCase(accountParams)) {
            is Outcome.Success -> {
                state = state.copy(
                    isLoading = false,
                    createdUser = user
                )
            }
            is Outcome.Failure -> {
                state = state.copy(
                    isLoading = false,
                    error = "User created but failed to create account"
                )
            }
        }
    }
}

data class UserState(
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val selectedRole: UserRole = UserRole.PARENT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val createdUser: User? = null
)
