package com.alokomkar.porter.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ReverseGeoCodeApi {

    @GET("/json")
    fun getStateCityFromLocation( @Query("latlng") latLng: String, @Query("api_key") apikey: String ) : Observable<LocationModel>

}
