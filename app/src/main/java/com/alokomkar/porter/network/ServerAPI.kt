package com.alokomkar.porter.network

import retrofit.Callback
import retrofit.client.Response
import retrofit.http.GET
import retrofit.http.Query

/**
 * Created by Alok on 09/04/18.
 */
interface ServerAPI {

    @GET("users/serviceability")
    fun getServiceAbility(callback: Callback<Response>)

    @GET("vehicles/cost")
    fun getVehiclesCost(@Query("lat") latitude : Double, @Query("lng") longitude : Double, callback: Callback<Response>)

    @GET("vehicles/eta")
    fun getVehiclesEta(@Query("lat") latitude : Double, @Query("lng") longitude : Double, callback: Callback<Response>)

}