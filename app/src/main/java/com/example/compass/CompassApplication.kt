package com.example.compass

import android.app.Application
import android.content.Context
import com.example.compass.about.aboutFragmentModule
import com.example.compass.about.aboutViewModelModule
import com.example.compass.api.networkModule
import com.example.compass.api.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin


class CompassApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidLogger()
            androidContext(this@CompassApplication)
            modules(
                repositoryModule,
                networkModule,
                aboutFragmentModule,
                aboutViewModelModule
            )
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}