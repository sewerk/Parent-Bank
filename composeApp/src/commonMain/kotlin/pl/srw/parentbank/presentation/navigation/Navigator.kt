package pl.srw.parentbank.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Simple navigator for managing screen navigation.
 */
class Navigator {
    var currentScreen by mutableStateOf<Screen>(Screen.Welcome)
        private set

    private val backStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        backStack.add(currentScreen)
        currentScreen = screen
    }

    fun navigateBack() {
        if (backStack.isNotEmpty()) {
            currentScreen = backStack.removeLast()
        }
    }

    fun canNavigateBack(): Boolean = backStack.isNotEmpty()
}

@Composable
fun rememberNavigator(): Navigator {
    return remember { Navigator() }
}
