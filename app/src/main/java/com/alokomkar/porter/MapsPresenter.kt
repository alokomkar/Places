package com.alokomkar.porter

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

/**
 * Created by Alok on 09/04/18.
 */
class MapsPresenter( private val mapsView : MapsView ) {


    private var mAddressBundle : Bundle?= null
    private var mAddressOutput: String? = null
    private var mAreaOutput: String? = null
    private var mCityOutput: String? = null

    private var mStateOutput1: String? = null
    private var mCountryOutput: String? = null
    private var mStreetOutput: String? = null
    private var mPinCode: String? = null

    inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            mAddressBundle = resultData

            mAddressOutput = resultData.getString(LocationUtils.LocationConstants.RESULT_DATA_KEY)
            mStreetOutput = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_STREET)
            mAreaOutput = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_AREA)
            mCityOutput = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_CITY)
            mStateOutput1 = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_STATE)
            mPinCode = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_PINCODE)
            mCountryOutput = resultData.getString(LocationUtils.LocationConstants.LOCATION_DATA_COUNTRY)

            mapsView.setCurrentAddress( mAddressOutput!! )


        }

    }

    fun getAddressResultReceiver(handler: Handler): AddressResultReceiver {
        return AddressResultReceiver(handler)
    }

}