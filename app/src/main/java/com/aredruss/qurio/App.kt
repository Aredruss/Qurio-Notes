package com.aredruss.qurio

import android.app.Application
import com.aredruss.qurio.di.databaseModule
import com.aredruss.qurio.di.domainModule
import com.aredruss.qurio.di.uiModule
import com.aredruss.qurio.helpers.LineLoggingTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(databaseModule, domainModule, uiModule)
        }

        Timber.plant(LineLoggingTree())
        Timber.tag("Qurio").i("App started")
    }
}