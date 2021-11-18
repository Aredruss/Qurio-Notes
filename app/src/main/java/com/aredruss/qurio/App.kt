package com.aredruss.qurio

import android.app.Application
import androidx.room.Room
import com.aredruss.qurio.di.databaseModule
import com.aredruss.qurio.di.domainModule
import com.aredruss.qurio.di.uiModule
import com.aredruss.qurio.domain.database.QurioDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    private lateinit var appDatabase: QurioDatabase

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(databaseModule, domainModule, uiModule)
        }
    }
}