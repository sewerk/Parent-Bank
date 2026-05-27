package pl.srw.parentbank.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import pl.srw.parentbank.presentation.navigation.AppNavigation
import pl.srw.parentbank.presentation.setup.FamilySetupViewModel

@Composable
@Preview
fun App(viewModel: FamilySetupViewModel) {
    MaterialTheme {
        AppNavigation(viewModel)
    }
}
