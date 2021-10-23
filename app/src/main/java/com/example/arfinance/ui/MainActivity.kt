package com.example.arfinance.ui

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.arfinance.R
import com.example.arfinance.databinding.ActivityMainBinding
import com.example.arfinance.util.setLocale
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        setupActionBarWithNavController(navController)

        setDefaultTheme()

        setDefaultLanguage()

        setDefaultColorAppearance()
    }

    private fun setDefaultLanguage() {
        val preference = PreferenceManager.getDefaultSharedPreferences(this)

        when(preference.getString(getString(R.string.language_key), "") ?: "") {
            getString(R.string.english) -> {
                setLocale(this,"en")
            }
            getString(R.string.persian) ->{
                setLocale(this,"fa")
            }
            else -> {
                setLocale(this,"en")
            }
        }
    }

    private fun setDefaultTheme() {
        val preference = PreferenceManager.getDefaultSharedPreferences(this)

        when(preference.getString(getString(R.string.theme_key), "") ?: "") {
            getString(R.string.dark) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.light) ->{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun setDefaultColorAppearance() {
        val preference = PreferenceManager.getDefaultSharedPreferences(this)

        when(preference.getString(getString(R.string.color_key), "") ?: "") {
            getString(R.string.orange) -> setTheme(R.style.Theme_ArFinance)
            getString(R.string.blue) -> setTheme(R.style.Theme_ArFinance_Blue)
            getString(R.string.green) -> setTheme(R.style.Theme_ArFinance_Green)
            getString(R.string.yellow) -> setTheme(R.style.Theme_ArFinance_Yellow)
            getString(R.string.purple) -> setTheme(R.style.Theme_ArFinance_Purple)
        }
    }


    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp()


}