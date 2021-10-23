package com.example.arfinance.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.preference.PreferenceManager
import com.example.arfinance.R
import com.example.arfinance.util.UiUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp()
class Application :Application(){

    override fun onCreate() {
        super.onCreate()
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        LeakCanary.install(this)*/
    }
}