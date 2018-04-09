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
class ApiGenerator {

    companion object {

        private var responseStatus: String? = null
        private var BASE_URL = "http://maps.googleapis.com/maps/api/geocode/"


        fun <S> createService(serviceClass: Class<S>): S {

            val okHttpClient = OkHttpClient()

            val builder = RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog { msg -> Log.d("ApiGenerator", msg)
                        Log.d("ApiGenerator",msg)
                        responseStatus = if(msg.contains("ZERO_RESULTS")) {
                            "Invalid"
                        } else
                            "Valid"
                    }
                    .setClient(OkClient(okHttpClient))
            val adapter = builder.build()
            return adapter.create(serviceClass)
        }

        private var SERVER_URL = "https://assignment-mobileapi.porter.in/"
        fun <S> createApiService(serviceClass: Class<S>): S {

            val okHttpClient = OkHttpClient()

            val builder = RestAdapter.Builder()
                    .setEndpoint(SERVER_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog { msg -> Log.d("ApiGenerator", msg)
                        Log.d("ApiGenerator",msg)
                    }
                    .setClient(OkClient(okHttpClient))
            val adapter = builder.build()
            return adapter.create(serviceClass)
        }

    }
}