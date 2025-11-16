package pl.srw.parentbank.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import pl.srw.parentbank.di.appModule

private var koinInitialized = false

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin once
    if (!koinInitialized) {
        startKoin {
            modules(appModule())
        }
        koinInitialized = true
    }
    App()
}