package com.johanespino.familyconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    var hisUid: String? = null
    var myUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val intent = intent
        hisUid = intent.getStringExtra("SuUID")
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activity_map) as SupportMapFragment

        mapFragment.onCreate(savedInstanceState)
        mapFragment.onResume()

        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        map?.let {
            googleMap = it
        }

        val myMarker = LatLng(11.0, 12.0)
        val otherMarker = LatLng(11.001, 12.0)
        val zoomLevel = 16.0f

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
        map?.addMarker(MarkerOptions().position(myMarker).title("Yo"))
        map?.addMarker(MarkerOptions().position(otherMarker).title("Otro usuario"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
    }
}