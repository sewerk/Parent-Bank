package pl.srw.parentbank.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import pl.srw.parentbank.presentation.setup.AddMemberScreen
import pl.srw.parentbank.presentation.setup.FamilySetupScreen
import pl.srw.parentbank.presentation.setup.FamilySetupViewModel
import pl.srw.parentbank.presentation.setup.MembersListScreen

object Routes {
    const val FAMILY_SETUP = "familySetup"
    const val ADD_MEMBERS = "addMembers"
    const val MEMBERS_LIST = "membersList"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel = koinViewModel<FamilySetupViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = Routes.FAMILY_SETUP) {
        composable(Routes.FAMILY_SETUP) {
            FamilySetupScreen(
                uiState = uiState,
                onCreateFamily = viewModel::createFamily,
                onErrorShown = viewModel::clearError
            )

            LaunchedEffect(uiState.familyId) {
                if (uiState.familyId != null) {
                    navController.navigate(Routes.ADD_MEMBERS) {
                        popUpTo(Routes.FAMILY_SETUP) { inclusive = true }
                    }
                }
            }
        }

        composable(Routes.ADD_MEMBERS) {
            AddMemberScreen(
                uiState = uiState,
                onAddMember = viewModel::addMember,
                onDone = {
                    navController.navigate(Routes.MEMBERS_LIST) {
                        popUpTo(Routes.ADD_MEMBERS) { inclusive = true }
                    }
                },
                onErrorShown = viewModel::clearError
            )
        }

        composable(Routes.MEMBERS_LIST) {
            MembersListScreen(uiState = uiState)
        }
    }
}
