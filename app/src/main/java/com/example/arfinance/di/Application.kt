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
       /* val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UiUtil.showToast(applicationContext, preference.getString("theme", "") ?: "")
        setTheme(preference.getString("theme", "") ?: "")*/
    }

/*    private fun setTheme(newValue: String) {
        when (newValue) {
            getString(R.string.dark) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.light) -> {
                updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                if (BuildCompat.isAtLeastQ()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
            }
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        return true
    }*/
}
