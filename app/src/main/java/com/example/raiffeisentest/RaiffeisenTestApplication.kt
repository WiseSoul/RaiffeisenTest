package com.example.raiffeisentest

import android.app.Application
import com.example.raiffeisentest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/** Initializes application-scoped dependency injection. */
class RaiffeisenTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RaiffeisenTestApplication)
            modules(appModule)
        }
    }
}
