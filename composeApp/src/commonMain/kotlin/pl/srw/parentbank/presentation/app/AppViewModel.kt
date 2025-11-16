package pl.srw.parentbank.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.model.UserSession
import pl.srw.parentbank.domain.usecase.session.ClearSessionUseCase
import pl.srw.parentbank.domain.usecase.session.GetSessionUseCase
import pl.srw.parentbank.domain.usecase.session.SaveSessionUseCase
import pl.srw.parentbank.util.Outcome

/**
 * App-level ViewModel managing global state like user session.
 */
class AppViewModel(
    private val getSessionUseCase: GetSessionUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val clearSessionUseCase: ClearSessionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = getSessionUseCase()) {
                is Outcome.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        currentSession = result.data
                    )
                }
                is Outcome.Failure -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        currentSession = null
                    )
                }
            }
        }
    }

    fun onUserLoggedIn(user: User) {
        viewModelScope.launch {
            saveSessionUseCase(user)
            val session = UserSession(
                userId = user.id,
                familyId = user.familyId,
                userName = user.name,
                userRole = user.role,
                lastLoginAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            )
            _state.value = _state.value.copy(currentSession = session)
        }
    }

    fun logout() {
        viewModelScope.launch {
            clearSessionUseCase()
            _state.value = _state.value.copy(currentSession = null)
        }
    }
}

data class AppState(
    val isLoading: Boolean = true,
    val currentSession: UserSession? = null
) {
    val isLoggedIn: Boolean get() = currentSession != null
    val isParent: Boolean get() = currentSession?.userRole == UserRole.PARENT
    val isChild: Boolean get() = currentSession?.userRole == UserRole.CHILD
}
