package com.example.arfinance.ui.setting

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.arfinance.R
import com.example.arfinance.databinding.ColorPickerDialogBinding
import com.example.arfinance.util.setLocale
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //setPreferencesFromResource(R.xml.root_preferences, rootKey)
        addPreferencesFromResource(R.xml.root_preferences)

        val themePreference = findPreference<ListPreference>(getString(R.string.theme_key))
        themePreference?.onPreferenceChangeListener = themeChangeListener

        val languagePreference = findPreference<ListPreference>(getString(R.string.language_key))
        languagePreference?.onPreferenceChangeListener = languageChangeListener

        setValues(themePreference, languagePreference)

        val preference = findPreference<Preference>(getString(R.string.color_key))
        preference?.setOnPreferenceClickListener {
            // showColorPickerDialog()
            showDialog()
            return@setOnPreferenceClickListener true
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.color_picker_dialog, null)
        val dialogBinding: ColorPickerDialogBinding = ColorPickerDialogBinding.bind(dialogView)

        val customDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setTitle("Choose color")
            .show()
        val prefs: SharedPreferences.Editor =
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()

        dialogBinding.orangeColor.setOnClickListener {
            requireContext().theme.applyStyle(R.style.Theme_ArFinance, true)
            confirmColorSelection(prefs, getString(R.string.orange), customDialog)
        }
        dialogBinding.blueColor.setOnClickListener {
            requireContext().theme.applyStyle(R.style.Theme_ArFinance_Blue, true)
            confirmColorSelection(prefs, getString(R.string.blue), customDialog)
        }
        dialogBinding.greenColor.setOnClickListener {
            requireContext().theme.applyStyle(R.style.Theme_ArFinance_Green, true)
            confirmColorSelection(prefs, getString(R.string.green), customDialog)
        }
        dialogBinding.yellowColor.setOnClickListener {
            requireContext().theme.applyStyle(R.style.Theme_ArFinance_Yellow, true)
            confirmColorSelection(prefs, getString(R.string.yellow), customDialog)
        }
        dialogBinding.purpleColor.setOnClickListener {
            requireContext().theme.applyStyle(R.style.Theme_ArFinance_Purple, true)
            confirmColorSelection(prefs, getString(R.string.purple), customDialog)
        }

    }

    private fun showColorPickerDialog() {
        ColorPickerDialog.Builder(context)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(getString(R.string.confirm),
                ColorEnvelopeListener { envelope, fromUser ->
                    //TODO change color accent here


                })
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }

    private fun confirmColorSelection(
        prefs: SharedPreferences.Editor,
        color: String,
        dialog: AlertDialog
    ) {

        prefs.apply {
            putString(getString(R.string.color_key), color)
            commit()
        }
        dialog.dismiss()
        findNavController().navigateUp()
    }


    private val languageChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
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
        Preference.OnPreferenceChangeListener { _, newValue ->
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
            //findNavController().navigateUp()
            true
        }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

    fun setValues(themePreference: ListPreference?, languagePreference: ListPreference?) {
        val pr = PreferenceManager.getDefaultSharedPreferences(activity)
        val theme = pr.getString(
            getString(R.string.theme_key),
            getString(R.string.system_default)
        )
        val lang = pr.getString(
            getString(R.string.language_key),
            getString(R.string.english)
        )
        Log.e("PASHM", "$theme $lang")

        themePreference?.value = theme
        languagePreference?.value = lang
    }
}