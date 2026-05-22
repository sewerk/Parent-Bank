package pl.srw.parentbank.app

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import pl.srw.parentbank.di.appModule

fun MainViewController() = run {
    initKoin()
    ComposeUIViewController { App() }
}

private fun initKoin() {
    if (GlobalContext.getOrNull() == null) {
        startKoin {
            modules(appModule())
        }
    }
}
