package com.johanespino.familyconnect

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback{
    private lateinit var  googleMap: GoogleMap

    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    // que es el permission ID?
    private val PERMISSION_ID=42;
    private lateinit var dataBase: FirebaseFirestore
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getLastLocation(dataBase)
        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        uid = user!!.uid
        dataBase = FirebaseFirestore.getInstance()
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())

        getLastLocation(dataBase)

        return view
    }

    private fun getLastLocation(database: FirebaseFirestore) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
            )
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if(location != null){
                    Log.e("LOCATION", "Latitud: ${location.latitude} - Longitud: ${location.longitude}")

                    // OBTENER EL ID DEL USUARIO

                    val dbref =
                        database.collection("users").document(uid)
                    val hashMap =
                        HashMap<String, Any>()
                    hashMap["lat"] = location.latitude
                    hashMap["lng"] = location.longitude

                    dbref.update(hashMap)

                }

            }
    }

    private fun updateLocationInDB () {

    }

    override fun onMapReady(map: GoogleMap?) {
        var lat: Double = 0.0
        var lng: Double = 0.0

        map?.let {
            googleMap = it
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if(location != null){
                    Log.e("LOCATION", "Latitud: ${location.latitude} - Longitud: ${location.longitude}")
                    lat = location.latitude
                    lng = location.longitude

                }

            }


        val myMarker = LatLng(lat,lng)
        val zoomLevel = 16.0f //This goes up to 21

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
        map?.addMarker(MarkerOptions().position(myMarker).title("Aqui estoy"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
    }
}