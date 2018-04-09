package com.alokomkar.porter.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat

/**
 * Created by Alok on 09/04/18.
 */
fun handleMultiplePermission(context: Context, permissions: ArrayList<String>): Boolean {
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
    }
    return true
}