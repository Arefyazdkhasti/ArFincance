package com.example.arfinance.util

import android.content.Context
import android.net.Uri
import androidx.appcompat.widget.SearchView
import com.example.arfinance.BuildConfig


val <T> T.exhaustive: T
    get() = this

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}


fun getURLForResource(resourceId: Int): String {
    //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
    return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + resourceId)
        .toString()
}

fun drawbleToString(context: Context) {

    val id: Int = context.resources.getIdentifier("picture0001", "drawable", context.packageName)
}