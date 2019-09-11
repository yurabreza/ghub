package co.me.ghub

import android.app.Application
import co.me.ghub.di.applicationModule
import co.me.ghub.di.dataModule
import co.me.ghub.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(networkModule, applicationModule, dataModule))
        }

    }
}