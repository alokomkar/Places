package com.alokomkar.porter

/**
 * Created by Alok on 09/04/18.
 */
interface MapsView : BaseView {
    fun setCurrentAddress( locationAddress : String )
    fun onServiceResult( isServicable : Boolean )
    fun onVehicleCost( cost : Int )
    fun onVehicleETA( eta : Int )
}