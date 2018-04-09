package com.alokomkar.porter.network

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast

/**
 * Created by Alok on 09/04/18.
 */
fun View.isVisible() = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.showToast( message : String ) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT ).show()
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}