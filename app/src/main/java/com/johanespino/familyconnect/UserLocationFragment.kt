package com.johanespino.familyconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_user_location.*


class UserLocationFragment : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var  googleMap: GoogleMap
    private lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val zoomLevel = 16.0f

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
        map?.addMarker(MarkerOptions().position(myMarker).title("Aqui estoy"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
    }


}