package com.johanespino.familyconnect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_user_location.*

class UserLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var  googleMap: GoogleMap
    private lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_user_location) as SupportMapFragment
        mapFragment.getMapAsync(this)

        map_user_location.onCreate(savedInstanceState)
        map_user_location.onResume()

        map_user_location.getMapAsync(this)
        mFausedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
        }

        val myMarker = LatLng(11.0, 12.0)
        val zoomLevel = 16.0f //This goes up to 21

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
        map?.addMarker(MarkerOptions().position(myMarker).title("Aqui estoy"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
    }
}