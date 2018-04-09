package com.alokomkar.porter.network

import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Alok on 09/04/18.
 */
interface ServerAPI {

    @GET("users/serviceability")
    fun getServiceAbility() : Observable<JSONObject>

    @GET("vehicles/cost")
    fun getVehiclesCost(@Query("lat") latitude : Double, @Query("lng") longitude : Double) : Observable<JSONObject>

    @GET("vehicles/eta")
    fun getVehiclesEta(@Query("lat") latitude : Double, @Query("lng") longitude : Double) : Observable<JSONObject>

}