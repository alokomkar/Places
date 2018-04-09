package com.alokomkar.porter

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.alokomkar.porter.network.Cost
import com.alokomkar.porter.network.ETA
import com.alokomkar.porter.network.ServerAPI
import com.alokomkar.porter.network.Serviceable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

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

    private var mServerAPI : ServerAPI = PorterApplication.getServerApI()!!
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

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

    fun getServiceAbility( delayInSeconds : Long ) {
        if( delayInSeconds == 0L ) {
            mServerAPI.getServiceAbility().subscribeOn(Schedulers.io())
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe( getServicable() )
        }
        else {
            mServerAPI.getServiceAbility().subscribeOn(Schedulers.io())
                    .repeat()
                    .delay( delayInSeconds, TimeUnit.SECONDS )
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe( getServicable() )
        }

    }

    fun getVehicleCost( latitude : Double, longitude : Double ) {
        mServerAPI.getVehiclesCost(latitude, longitude).subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe( getCost() )
    }

    private fun getCost(): Observer<Cost> {

        return object : Observer<Cost> {

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: Cost) {
                mapsView.onVehicleCost(t.cost)
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onComplete() {

            }

        }

    }

    fun getVehicleETA( latitude : Double, longitude : Double ) {
        mServerAPI.getVehiclesEta( latitude, longitude ).subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe( getETA() )
    }

    private fun getETA( ): Observer<ETA> {

        return object : Observer<ETA> {

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: ETA) {
                mapsView.onVehicleETA(t.eta)
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onComplete() {

            }

        }

    }

    private fun getServicable( ): Observer<Serviceable> {

        return object : Observer<Serviceable> {

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: Serviceable) {
                mapsView.onServiceResult(t.serviceable)
            }

            override fun onSubscribe(d: Disposable) {
                mCompositeDisposable.add(d)
            }

            override fun onComplete() {

            }

        }

    }

    fun onDestroy() {
        mCompositeDisposable.dispose()
    }


}