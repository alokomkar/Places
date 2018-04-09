package com.alokomkar.porter

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil

/**
 * Created by Alok on 09/04/18.
 */
class LocationUtils {


    object LocationConstants {

        val SUCCESS_RESULT = 0
        val FAILURE_RESULT = 1

        val PACKAGE_NAME = BuildConfig.APPLICATION_ID

        val RECEIVER = PACKAGE_NAME + ".RECEIVER"
        val RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY"
        val LOCATION_DATA_PINCODE = PACKAGE_NAME + ".LOCATION_DATA_PINCODE"
        val LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA"
        val LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA"
        val LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY"
        val LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET"

        val LOCATION_DATA_STATE = PACKAGE_NAME + ".LOCATION_DATA_STATE"
        val LOCATION_DATA_COUNTRY = ".LOCATION_DATA_COUNTRY"
    }

    companion object {


        fun hasLollipop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

        fun isLocationEnabled(context: Context): Boolean {
            var locationMode = 0
            val locationProviders: String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE)

                } catch (e: Settings.SettingNotFoundException) {
                    e.printStackTrace()
                }

                return locationMode != Settings.Secure.LOCATION_MODE_OFF
            } else {
                locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
                return !TextUtils.isEmpty(locationProviders)
            }
        }

        fun checkPlayServices( activity : AppCompatActivity ): Boolean {
            val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                            9000).show()
                } else {
                    //finish();
                }
                return false
            }
            return true
        }
    }

}