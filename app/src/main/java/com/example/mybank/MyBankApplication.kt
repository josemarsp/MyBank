package com.example.mybank

import android.app.Application
import com.example.mybank.di.bankModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyBankApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyBankApplication)
            AndroidLogger(Level.INFO)
            modules(bankModules)
        }
    }
}

