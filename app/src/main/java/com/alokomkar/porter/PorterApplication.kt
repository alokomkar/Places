package com.alokomkar.porter

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.alokomkar.porter.network.NetModule
import com.alokomkar.porter.network.ReverseGeoCodeApi
import com.alokomkar.porter.network.ServerAPI

/**
 * Created by Alok on 09/04/18.
 */
class PorterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {

        var serverAPI: ServerAPI?= null
        fun getServerApI() : ServerAPI? {
            if( serverAPI == null ) serverAPI = NetModule(instance).serverAPI
            return serverAPI
        }

        var reverseGeoCodeApi: ReverseGeoCodeApi?= null
        fun getGeoCodeApi() : ReverseGeoCodeApi? {
            if( reverseGeoCodeApi == null ) reverseGeoCodeApi = NetModule(instance).reverseGeoCodeAPI
            return reverseGeoCodeApi
        }

        lateinit var instance: PorterApplication
            private set
    }
}