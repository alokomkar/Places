package com.alokomkar.porter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        tvFrom.setOnClickListener {  }
        tvTo.setOnClickListener {  }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val locationLatLng = LatLng(12.9716, 77.5946)
        mMap.addMarker(MarkerOptions().position(locationLatLng).title("Marker in Bangalore"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationLatLng))
    }
}
