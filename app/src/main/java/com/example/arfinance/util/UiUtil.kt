package com.example.arfinance.util

import androidx.fragment.app.Fragment
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class UiUtil {
    companion object {

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun showSnackBar(root: View, msg: String) {
            Snackbar.make(root, msg, Snackbar.LENGTH_LONG).show()
        }


    }

}