package com.alokomkar.porter.network

/**
 * Created by Alok on 09/04/18.
 */
import android.util.Log
import com.squareup.okhttp.OkHttpClient

import retrofit.RestAdapter
import retrofit.client.OkClient

/**
 * Created by varun on 9/20/17.
 */
// No need to instantiate this class.
class LocationApiGenerator {
    companion object {

        var locationStatus: String? = null
        var BASE_URL = "http://maps.googleapis.com/maps/api/geocode/"
        var PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/"
        var PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/"

        fun <S> placesService(serviceClass: Class<S>): S {

            val okHttpClient = OkHttpClient()

            val builder = RestAdapter.Builder()
                    .setEndpoint(PLACES_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog { msg -> Log.d("Retro", msg)
                        Log.d("Retro",msg)
                        if(msg.contains("ZERO_RESULTS",ignoreCase = false)) {
                            Log.i("RETRO RESULT",".......Invalid location verified")
                            locationStatus = "Invalid"
                        } else
                            locationStatus = "Valid"
                    }
                    .setClient(OkClient(okHttpClient))
            val adapter = builder.build()
            return adapter.create(serviceClass)
        }

        fun <S> createService(serviceClass: Class<S>): S {

            val okHttpClient = OkHttpClient()

            val builder = RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog { msg -> Log.d("Retro", msg)
                        Log.d("Retro",msg)
                        if(msg.contains("ZERO_RESULTS",ignoreCase = false)) {
                            Log.i("RETRO RESULT",".......Invalid location verified")
                            locationStatus = "Invalid"
                        } else
                            locationStatus = "Valid"
                    }
                    .setClient(OkClient(okHttpClient))
            val adapter = builder.build()
            return adapter.create(serviceClass)
        }

        fun <S> createPlacesService(serviceClass: Class<S>): S {

            val builder = RestAdapter.Builder()
                    .setEndpoint(PLACES_BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog { msg -> Log.i("Retro", msg) }
                    .setClient(OkClient(OkHttpClient()))

            val adapter = builder.build()
            return adapter.create(serviceClass)
        }
    }
}