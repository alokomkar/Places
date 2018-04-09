package com.alokomkar.porter.network

import retrofit.Callback
import retrofit.http.GET
import retrofit.http.Query

interface ReverseGeoCodeApi {
    @GET("/json")
    fun getStateCityFromLocation(@Query("latlng") latLng: String, @Query("api_key") apikey: String, callback: Callback<LocationModel>)

}
