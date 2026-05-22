package pl.srw.parentbank.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.srw.parentbank.domain.model.User
import pl.srw.parentbank.domain.model.UserRole
import pl.srw.parentbank.domain.usecase.AddFamilyMemberParams
import pl.srw.parentbank.domain.usecase.AddFamilyMemberUseCase
import pl.srw.parentbank.domain.usecase.CreateFamilyParams
import pl.srw.parentbank.domain.usecase.CreateFamilyUseCase
import pl.srw.parentbank.domain.usecase.GetFamilyMembersUseCase

data class FamilySetupUiState(
    val familyId: String? = null,
    val familyName: String = "",
    val familyCode: String = "",
    val members: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FamilySetupViewModel(
    private val createFamilyUseCase: CreateFamilyUseCase,
    private val addFamilyMemberUseCase: AddFamilyMemberUseCase,
    private val getFamilyMembersUseCase: GetFamilyMembersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FamilySetupUiState())
    val uiState: StateFlow<FamilySetupUiState> = _uiState.asStateFlow()

    fun createFamily(familyName: String, creatorName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            createFamilyUseCase(CreateFamilyParams(familyName, creatorName))
                .onSuccess { family ->
                    _uiState.update {
                        it.copy(
                            familyId = family.id,
                            familyName = family.name,
                            familyCode = family.familyCode,
                            isLoading = false
                        )
                    }
                    refreshMembers()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun addMember(name: String, role: UserRole, age: Int?) {
        val familyId = _uiState.value.familyId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            addFamilyMemberUseCase(AddFamilyMemberParams(familyId, name, role, age))
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    refreshMembers()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun refreshMembers() {
        val familyId = _uiState.value.familyId ?: return
        viewModelScope.launch {
            getFamilyMembersUseCase(familyId)
                .onSuccess { members ->
                    _uiState.update { it.copy(members = members) }
                }
        }
    }
}
