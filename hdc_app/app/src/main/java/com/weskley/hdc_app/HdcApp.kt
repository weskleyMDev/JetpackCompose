package com.weskley.hdc_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HdcApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}