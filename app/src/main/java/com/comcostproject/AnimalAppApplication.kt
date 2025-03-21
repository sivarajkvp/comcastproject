package com.comcostproject

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AnimalAppApplication :Application() {
    override fun onCreate() {
        super.onCreate()
    }
}