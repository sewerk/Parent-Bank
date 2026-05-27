package pl.srw.parentbank.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import pl.srw.parentbank.di.appModule

private var koinStarted = false

fun MainViewController() = run {
    if (!koinStarted) {
        startKoin { modules(appModule()) }
        koinStarted = true
    }
    ComposeUIViewController { App() }
}
