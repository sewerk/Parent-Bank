package pl.srw.parentbank.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform
import pl.srw.parentbank.di.appModule
import pl.srw.parentbank.presentation.setup.FamilySetupViewModel

private var koinStarted = false

fun MainViewController() = run {
    if (!koinStarted) {
        startKoin { modules(appModule()) }
        koinStarted = true
    }
    val viewModel: FamilySetupViewModel = KoinPlatform.getKoin().get()
    ComposeUIViewController { App(viewModel) }
}
