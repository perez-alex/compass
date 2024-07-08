package com.example.compass.api

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import org.koin.dsl.module

val repositoryModule = module {
    factory { CompassRepository(get(), get()) }
}

class CompassRepository(private val compassApi: CompassApi, context: Context) {

    private val sharedPreferences = context.getSharedPreferences("CompassCache", Context.MODE_PRIVATE)
    private val ABOUT_PAGE_KEY = "aboutPage"

    suspend fun getAboutPage(): String {
        var aboutPage = ""
        // Check cache first
        sharedPreferences.getString(ABOUT_PAGE_KEY, null)?.let {
            if (it.isNotEmpty()) {
                aboutPage = it
            }
        }

        // Fetch from network and cache
        try {
            val remoteAboutPage = compassApi.getAboutPage()
            remoteAboutPage.body()?.let {
                aboutPage = it.string()

                // Cache the fetched about page
                sharedPreferences.edit { putString(ABOUT_PAGE_KEY, aboutPage) }
            }
        } catch (e: NoConnectivityException) {
            Log.e("tag", "No internet connection available")
        }

        return aboutPage
    }
}