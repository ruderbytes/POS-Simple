package id.lemonavy.dalenta.base

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import id.lemonavy.dalenta.di.baseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
@HiltAndroidApp
class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            androidLogger(Level.NONE)
            androidContext(this@BaseApp)
            modules(
                listOf(
                    baseModule
                )
            )
        }
    }
}