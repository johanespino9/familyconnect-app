package com.johanespino.familyconnect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback{
    private lateinit var  googleMap: GoogleMap
    private lateinit var lat: TextView
    private lateinit var log: TextView
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    // que es el permission ID?
    private val PERMISSION_ID=42;
    private lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        lat = view.findViewById(R.id.tv_map_latitude)
        log = view.findViewById(R.id.tv_map_longitude)
        mFausedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastlocation();
        mLocationCallback
        return view
    }

    override fun onResume() {
        super.onResume()

        getLastlocation()
        mLocationCallback
    }

    //Revisar el estado del usuario
    private fun checkUserStatus(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            //Si esta logeado se mantiene aqui
        } else {
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }}

    @SuppressLint("MissingPermission")
    private fun getLastlocation() {
        if(checkPermission()){
            if(isLocationEnabled()){
                mFausedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()){task ->
                    val location: Location?= task.result
                    if(location == null){
                        requestNewLocationgData()
                    }else{
                        lat.text= location.latitude.toString();
                        log.text=location.longitude.toString();
                        latitude = location.latitude
                        longitude = location.longitude
                    }

                }
            }else{
                Toast.makeText(context,"Enciende tu ubicacion", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }
    @SuppressLint( "MissingPermission")
    private  fun requestNewLocationgData(){
        var mLocationRequet = LocationRequest();
        mLocationRequet.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequet.interval=0
        mLocationRequet.fastestInterval=0
        mLocationRequet.numUpdates=1

        mFausedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())
        mFausedLocationProviderClient.requestLocationUpdates(
            mLocationRequet,mLocationCallback,
            Looper.myLooper()
        )
    }

    private  val mLocationCallback=object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var mLastLocation:Location=p0.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            lat.text= mLastLocation.latitude.toString();
            log.text=mLastLocation.longitude.toString();
        }
    }

    private fun isLocationEnabled():Boolean{
        val locationManger: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private  fun checkPermission():Boolean{
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastlocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_post).isVisible = false
        menu.findItem(R.id.action_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_logout -> {
                Firebase.auth.signOut()
                checkUserStatus()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        checkUserStatus()
        getLastlocation()
        super.onStart()
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
        }

        val myMarker = LatLng(latitude, longitude)
        val zoomLevel = 16.0f //This goes up to 21

        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
        map?.addMarker(MarkerOptions().position(myMarker).title("Aqui estoy"))
        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
    }
}



//package com.johanespino.familyconnect
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.location.Location
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.app.ActivityCompat
//import androidx.fragment.app.Fragment
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.android.synthetic.main.fragment_map.*
//import java.util.*
//
//
//class MapFragment : Fragment(), OnMapReadyCallback{
//    private lateinit var  googleMap: GoogleMap
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var uid: String
//    // que es el permission ID?
//    private val PERMISSION_ID=42;
//    private lateinit var dataBase: FirebaseFirestore
//    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        getLastLocation(dataBase)
//        map_view.onCreate(savedInstanceState)
//        map_view.onResume()
//
//        map_view.getMapAsync(this)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//
//    ): View? {
//        // Inflate the layout for this fragment
//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//        uid = user!!.uid
//        dataBase = FirebaseFirestore.getInstance()
//        val view = inflater.inflate(R.layout.fragment_map, container, false)
//        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        getLastLocation(dataBase)
//
//        return view
//    }

//    private fun getLastLocation(database: FirebaseFirestore) {
//
//        if (requestPermissions()) return
//        mFusedLocationProviderClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                // Got last known location. In some rare situations this can be null.
//                if(location != null){
//                    Log.e("LOCATION", "Latitud: ${location.latitude} - Longitud: ${location.longitude}")
//
//                    // OBTENER EL ID DEL USUARIO
//
//                    val dbref =
//                        database.collection("users").document(uid)
//                    val hashMap =
//                        HashMap<String, Any>()
//                    hashMap["lat"] = location.latitude
//                    hashMap["lng"] = location.longitude
//
//                    dbref.update(hashMap)
//
//                }
//
//            }
//    }

//    private fun requestPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ),
//                PERMISSION_ID
//            )
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return true
//        }
//        return false
//    }

//    private fun updateLocationInDB () {
//
//    }
//
//    override fun onMapReady(map: GoogleMap?) {
//        var lat: Double = 0.0
//        var lng: Double = 0.0
//
//        var longitud: Double
//        var latitud: Double
//
//        map?.let {
//            googleMap = it
//        }
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        mFusedLocationProviderClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                // Got last known location. In some rare situations this can be null.
//                if(location != null){
//                    Log.e("LOCATION", "Latitud: ${location.latitude} - Longitud: ${location.longitude}")
//                    latitud = location.latitude
//                    longitud = location.longitude
//
//                }
//
//            }
//
//
//        val myMarker = LatLng(latitud,longitud)
//        val zoomLevel = 16.0f //This goes up to 21
//
//        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
//        map?.addMarker(MarkerOptions().position(myMarker).title("Aqui estoy"))
//        map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
//    }
//}