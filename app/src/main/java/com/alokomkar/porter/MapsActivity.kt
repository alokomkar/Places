package com.alokomkar.porter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.alokomkar.porter.LocationUtils.Companion.checkPlayServices
import com.alokomkar.porter.network.hide
import com.alokomkar.porter.network.show
import com.alokomkar.porter.network.showToast
import com.alokomkar.porter.utils.handleMultiplePermission
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.model.CameraPosition

@SuppressLint("SetTextI18n")
@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(),
        OnMapReadyCallback,
        PlaceSelectionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        MapsView {


    private lateinit var mMap: GoogleMap
    private lateinit var mResultReceiver: MapsPresenter.AddressResultReceiver
    private lateinit var mGoogleApiClient : GoogleApiClient
    private var tvCurrentPlace: TextView ?= null
    private val PERMISSIONS_REQUEST_CODE_LOCATION = 113
    private lateinit var mMapsPresenter : MapsPresenter
    private var mCost : Int = 0
    private var mETA : Int = 0

    override fun showProgress(message: String) {
        if( progressLayout != null )
            progressLayout.show()
    }

    override fun hideProgress() {
        if( progressLayout != null )
            progressLayout.hide()
    }

    override fun onError(error: String) {
        this.showToast( error )
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        if( result.errorMessage != null )
            onError( result.errorMessage!! )
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null)
            changeMap(location)
        LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this )
    }

    override fun onServiceResult(isServicable: Boolean) {

        if( isServicable ) tvBlocked.hide()
        else tvBlocked.show()

        if( isServicable )
        mMapsPresenter.getVehicleETA( toPlace!!.latLng.latitude, toPlace!!.latLng.longitude )

    }


    override fun onVehicleCost(cost: Int) {
        mCost = cost
        tvLocation.text = "Cost : $mCost : ETA : $mETA minutes | Book now"
    }

    override fun onVehicleETA(eta: Int) {
        mETA = eta
        tvLocation.text = "Cost : $mCost : ETA : $mETA minutes | Book now"
    }

    @SuppressLint("RestrictedApi")
    override fun onConnected(p0: Bundle?) {
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
            changeMap( mLastLocation )
        } else {
            LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, this )
        }

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this)

    }

    private fun changeMap(location: Location) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // check if map is created successfully or not
        mMap.isMyLocationEnabled = true
        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(location.latitude, location.longitude)).zoom(19f).tilt(70f).build()
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition))

        startIntentService(location)

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onError(status: Status?) {
        fragmentManager.popBackStack()
    }

    override fun setCurrentAddress(locationAddress: String) {
        if( tvCurrentPlace != null ) {
            tvCurrentPlace!!.text = locationAddress
        }
        tvLocation.text = locationAddress
    }

    private var fromPlace: Place? = null
    private var toPlace : Place? = null

    override fun onPlaceSelected(place: Place?) {
        if( place != null ) {
            if( tvCurrentPlace != null ) {
                placeMarkerForLocation( place.latLng.latitude, place.latLng.longitude )
                when( tvCurrentPlace!!.id ) {
                    R.id.tvFrom -> {
                        fromPlace = place
                    }
                    R.id.tvTo ->{
                        toPlace = place
                    }
                }

                if( fromPlace != null && toPlace != null ) {
                    mMapsPresenter.getServiceAbility()

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        showProgress(getString(R.string.loading))
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapsFragment = supportFragmentManager
                .findFragmentById(R.id.mapsFragment) as SupportMapFragment

        mapsFragment.getMapAsync(this)

        tvFrom.setOnClickListener {
            tvCurrentPlace = tvFrom
            openLocationsPicker()
        }

        tvTo.setOnClickListener {
            tvCurrentPlace = tvTo
            openLocationsPicker()
        }

        mMapsPresenter = MapsPresenter( this )
        mResultReceiver = mMapsPresenter.getAddressResultReceiver(Handler())

        val permissionList = arrayListOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!handleMultiplePermission(this, permissionList)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( permissionList.toTypedArray(), PERMISSIONS_REQUEST_CODE_LOCATION)
            }
        }
        else
            checkForPlayService()
    }

    private fun checkForPlayService() {
        if (checkPlayServices( this )) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!LocationUtils.isLocationEnabled(this)) {
                // notify user
                val dialog = AlertDialog.Builder(this)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton("Open location settings", { _, _ ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(myIntent, 1)
                })
                dialog.setNegativeButton("Cancel", { _, _ ->

                })
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            Toast.makeText(this, "Location not supported in this device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private val PLACE_AUTOCOMPLETE_REQUEST_CODE: Int = 1234

    private fun openLocationsPicker() {
        // Construct an intent for the place picker
        try {
            val intentBuilder =
                    PlacePicker.IntentBuilder()
            val intent = intentBuilder.build(this)
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)

        } catch ( e : GooglePlayServicesRepairableException) {
            // ...
            e.printStackTrace()
        } catch ( e : GooglePlayServicesNotAvailableException ) {
            // ...
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // The user has selected a place. Extract the name and address.
                val place = PlacePicker.getPlace(this, data)
                onPlaceSelected(place)
            }
        }
        else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        hideProgress()
        mMap = googleMap
        mMap.setOnCameraChangeListener { cameraPosition ->
            Log.d("Camera postion change" + "", cameraPosition.toString() + "")
            val latLong = cameraPosition.target
            mMap.clear()
            val mLocation = Location("")
            mLocation.latitude = latLong.latitude
            mLocation.longitude = latLong.longitude
            startIntentService(mLocation)
        }
        placeMarkerForLocation( 12.9716, 77.5946 )
    }

    private fun placeMarkerForLocation( latitude : Double, longitude : Double ) {
        val locationLatLng = LatLng( latitude, longitude )
        //mMap.addMarker(MarkerOptions().position(locationLatLng).title("Marker in Bangalore"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 12.0f))
        mMap.clear()
        val mLocation = Location("")
        mLocation.latitude = latitude
        mLocation.longitude = longitude
        startIntentService(mLocation)
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private fun startIntentService(mLocation: Location) {

        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java)

        // Pass the result receiver as an extra to the service.
        intent.putExtra(LocationUtils.LocationConstants.RECEIVER, mResultReceiver)

        // Pass the location data as an extra to the service.
        intent.putExtra(LocationUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        this.startService(intent)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if( requestCode == PERMISSIONS_REQUEST_CODE_LOCATION ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForPlayService()
            } else if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                val dialogBuilder = android.support.v7.app.AlertDialog.Builder(this)
                dialogBuilder.setTitle("Location Permission needed to use the app")
                dialogBuilder.setMessage("Allow app to access your location?")
                dialogBuilder.setPositiveButton("Open App Permission") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.packageName, null))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                dialogBuilder.setNegativeButton("Cancel") { _, _ ->
                    Toast.makeText(this, "Some permissions were denied", Toast.LENGTH_LONG).show()
                }
                val b = dialogBuilder.create()
                b.show()
            }
        }

    }


}
