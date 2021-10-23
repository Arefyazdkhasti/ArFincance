package com.example.arfinance.util

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast
import com.example.arfinance.R
import com.google.android.material.snackbar.Snackbar


class UiUtil {
    companion object {

        fun showToast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun showSnackBar(root: View, msg: String) {
            Snackbar.make(root, msg, Snackbar.LENGTH_LONG).show()
        }

        const val color = 0xff3b5998
    }

}
class Res(original: Resources,  private val newColor: Int) : Resources(original.assets, original.displayMetrics, original.configuration) {
    @Throws(NotFoundException::class)
    override fun getColor(id: Int): Int {
        return getColor(id, null)
    }

    @Throws(NotFoundException::class)
    override fun getColor(id: Int, theme: Theme?): Int {
        return when (getResourceEntryName(id)) {
            "colorAccent" ->                 // You can change the return value to an instance field that loads from SharedPreferences.
                newColor
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    super.getColor(id, theme)
                } else super.getColor(id)
            }
        }
    }
}