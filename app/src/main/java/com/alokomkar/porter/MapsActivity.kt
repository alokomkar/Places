package com.alokomkar.porter

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, PlaceSelectionListener {

    override fun onError(status: Status?) {
        fragmentManager.popBackStack()
    }

    override fun onPlaceSelected(place: Place?) {
        fragmentManager.popBackStack()
        if( etPlace != null )
            etPlace!!.setText( place!!.address )

        placeMarkerForLocation( place!!.latLng.latitude, place.latLng.longitude )

    }

    private lateinit var mMap: GoogleMap
    private var etPlace : TextInputEditText ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapsFragment = supportFragmentManager
                .findFragmentById(R.id.mapsFragment) as SupportMapFragment

        mapsFragment.getMapAsync(this)
        tilFrom.setOnClickListener {
            etPlace = etFrom
            loadPlacesFragment()
        }
        etFrom.setOnClickListener {
            etPlace = etFrom
            loadPlacesFragment()
        }
        etTo.setOnClickListener {
            etPlace = etTo
            loadPlacesFragment()
        }
        tilTo.setOnClickListener {
            etPlace = etFrom
            loadPlacesFragment()
        }
        loadPlacesFragment()
    }

    @SuppressLint("ResourceType")
    private fun loadPlacesFragment() {
        val placeAutoCompleteFragment = PlaceAutocompleteFragment()
        placeAutoCompleteFragment.setOnPlaceSelectedListener( this )
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.placesContainer, placeAutoCompleteFragment)
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up,
                R.anim.slide_in_down,
                R.anim.slide_out_down,
                R.anim.slide_out_up)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        placeMarkerForLocation( 12.9716, 77.5946 )
    }

    private fun placeMarkerForLocation( latitude : Double, longitude : Double ) {
        val locationLatLng = LatLng( latitude, longitude )
        mMap.addMarker(MarkerOptions().position(locationLatLng).title("Marker in Bangalore"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationLatLng))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12.0f))
    }
}
