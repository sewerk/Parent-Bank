package pl.srw.parentbank.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import pl.srw.parentbank.di.appModule

/**
 * Android Application class that initializes Koin dependency injection.
 */
class ParentBankApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ParentBankApplication)
            modules(appModule())
        }
    }
}
