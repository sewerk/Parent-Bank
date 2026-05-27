package pl.srw.parentbank.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pl.srw.parentbank.presentation.setup.AddMemberScreen
import pl.srw.parentbank.presentation.setup.FamilySetupScreen
import pl.srw.parentbank.presentation.setup.FamilySetupViewModel
import pl.srw.parentbank.presentation.setup.MembersListScreen

enum class Screen { FAMILY_SETUP, ADD_MEMBERS, MEMBERS_LIST }

@Composable
fun AppNavigation(viewModel: FamilySetupViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var currentScreen by remember { mutableStateOf(Screen.FAMILY_SETUP) }

    if (currentScreen == Screen.FAMILY_SETUP && uiState.familyId != null) {
        currentScreen = Screen.ADD_MEMBERS
    }

    when (currentScreen) {
        Screen.FAMILY_SETUP -> FamilySetupScreen(
            uiState = uiState,
            onCreateFamily = viewModel::createFamily,
            onErrorShown = viewModel::clearError
        )
        Screen.ADD_MEMBERS -> AddMemberScreen(
            uiState = uiState,
            onAddMember = viewModel::addMember,
            onDone = { currentScreen = Screen.MEMBERS_LIST },
            onErrorShown = viewModel::clearError
        )
        Screen.MEMBERS_LIST -> MembersListScreen(
            uiState = uiState
        )
    }
}
