package com.example.marek.sledzeniepaczek.activities

import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.marek.sledzeniepaczek.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        title = "Package location"
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val geocoder = Geocoder(this)
        val searchResult = geocoder.getFromLocationName(intent.getStringExtra("location"), 1)

        if (searchResult.size <= 0)
            return
        val location = searchResult[0]
        val sydney = LatLng(location.latitude, location.longitude)
        //val sydney = LatLng(-34.0, 151.0)
        mMap.setMinZoomPreference(10f)
        mMap.setMaxZoomPreference(12f)
        mMap.addMarker(MarkerOptions().position(sydney).title("Your package is here"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
