package com.example.arfinance.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.arfinance.R
import com.example.arfinance.util.setLocale

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themePreference = findPreference<ListPreference>(getString(R.string.theme_key))
        themePreference?.onPreferenceChangeListener = themeChangeListener

        val languagePreference = findPreference<ListPreference>(getString(R.string.language_key))
        languagePreference?.onPreferenceChangeListener = languageChangeListener

    }

    private val languageChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
        Log.i("newValue", newValue.toString())
        newValue as? String
        when (newValue) {
            getString(R.string.english) -> {
                setLocale(requireActivity(), "en")
            }
            getString(R.string.persian) -> {
                setLocale(requireActivity(), "fa")
            }
            else -> {
                setLocale(requireActivity(), "en")
            }
        }
            findNavController().navigateUp()
        true
    }

    private val themeChangeListener =
        Preference.OnPreferenceChangeListener { preference, newValue ->
            Log.i("newValue", newValue.toString())
            newValue as? String
            when (newValue) {
                getString(R.string.dark) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.light) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    if (BuildCompat.isAtLeastQ()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
            findNavController().navigateUp()
            true
        }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}