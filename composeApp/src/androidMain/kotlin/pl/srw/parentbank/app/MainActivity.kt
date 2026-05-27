package pl.srw.parentbank.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.srw.parentbank.di.appModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initKoin()
        setContent {
            App()
        }
    }

    private fun initKoin() {
        if (!koinStarted) {
            startKoin {
                androidContext(applicationContext)
                modules(appModule())
            }
            koinStarted = true
        }
    }

    companion object {
        private var koinStarted = false
    }
}
