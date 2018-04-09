package com.alokomkar.porter

/**
 * Created by Alok on 09/04/18.
 */
import android.app.IntentService
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import com.alokomkar.porter.network.LocationModel
import com.alokomkar.porter.network.ReverseGeoCodeApi
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
/**
 * This constructor is required, and calls the super IntentService(String)
 * constructor with the name for a worker thread.
 */
class FetchAddressIntentService : IntentService(TAG) {

    private var city: String? = null
    private var state: String? = null
    private var country: String? = null
    private var area: String? = null
    private var area1: String? = null

    private var street: String? = null
    /**
     * The receiver where results are forwarded from this service.
     */
    protected var mReceiver: ResultReceiver? = null
    private var postalcode: String? = null

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a [ResultReceiver] in * MainActivity to process content
     * sent from this service.
     *
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    override fun onHandleIntent(intent: Intent?) {
        var errorMessage = ""

        mReceiver = intent!!.getParcelableExtra(LocationUtils.LocationConstants.RECEIVER)
        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.")
            return
        }
        // Get the location passed to this service through an extra.
        val location : Location? = intent.getParcelableExtra(LocationUtils.LocationConstants.LOCATION_DATA_EXTRA)

        // Make sure that the location data was really sent over through an extra. If it wasn't,
        // send an error error message and return.
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided)
            Log.wtf(TAG, errorMessage)
            deliverResultToReceiver(LocationUtils.LocationConstants.FAILURE_RESULT, errorMessage, null, null, null, null, null, null)
            return
        }

        val reverseGeoCodeApi  : ReverseGeoCodeApi = PorterApplication.getGeoCodeApi()!!
        reverseGeoCodeApi.getStateCityFromLocation(location.latitude.toString() + "," + location.longitude.toString(),
                resources.getString(R.string.google_maps_key))
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe( getLocationObserver() )


    }

    private fun getLocationObserver(): Observer<LocationModel?> {
        return object : Observer<LocationModel?> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(locationModel: LocationModel) {
                if( locationModel.resultsList.isEmpty()) {
                    return
                }

                val addressComponentList = locationModel.resultsList.get(0).address_components

                if (addressComponentList!!.isEmpty()) {
                    deliverResultToReceiver(LocationUtils.LocationConstants.FAILURE_RESULT,
                            "Unable to fetch location",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);
                } else {


                    for (address in addressComponentList) {
                        Log.d("address", address.toString())
                        for (type in address.types!!) {


                            if (type.equals("street_number", ignoreCase = true)) {
                                street = address.long_name
                            }

                            if (type.equals("route", ignoreCase = true)) {
                                if (street == null)
                                    street = address.long_name
                                else
                                    street = street + " " + address.long_name
                            }

                            if (type.equals("postal_code", ignoreCase = true)) {
                                postalcode = address.long_name
                            }

                            if (type.equals("sublocality_level_2", ignoreCase = true)) {
                                if (street == null)
                                    street = address.long_name
                                else
                                    street = street + " " + address.long_name
                            }

                            if (type.equals("administrative_area_level_2", ignoreCase = true)) {
                                area1 = address.long_name
                            }

                            if (type.equals("locality", ignoreCase = true)) {
                                city = address.long_name
                            }
                            if (type.equals("administrative_area_level_1", ignoreCase = true)) {
                                state = address.long_name
                            }
                            if (type.equals("sublocality_level_1", ignoreCase = true)) {
                                area = address.long_name
                            }


                            if (type.equals("country", ignoreCase = true)) {
                                country = address.long_name
                            }


                        }


                    }

                    deliverResultToReceiver(LocationUtils.LocationConstants.SUCCESS_RESULT,
                            locationModel.resultsList.get(0).formatted_address,
                            city,
                            state,
                            country,
                            area,
                            street, postalcode)


                }
            }

            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                if( e.message != null ) {
                    Log.d(TAG, "Location Error : " + e.message )
                }
                e.printStackTrace()
            }

        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private fun deliverResultToReceiver(resultCode: Int,
                                        message: String?,
                                        city: String?,
                                        state: String?,
                                        country: String?,
                                        area: String?,
                                        street: String?,
                                        postalcode: String?) {
        try {
            val bundle = Bundle()
            bundle.putString(LocationUtils.LocationConstants.RESULT_DATA_KEY, message)
            if (street != null)
                bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_STREET, street)

            if (postalcode != null)
                bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_PINCODE, postalcode)

            if (area != null)
                bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_AREA, area)
            else
                bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_AREA, area1)

            bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_CITY, city)
            bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_STATE, state)
            bundle.putString(LocationUtils.LocationConstants.LOCATION_DATA_COUNTRY, country)
            mReceiver!!.send(resultCode, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = "FetchAddressIS"
    }


}// Use the TAG to name the worker thread.
