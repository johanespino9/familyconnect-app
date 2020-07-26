package com.johanespino.familyconnect.Fragments

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
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.johanespino.familyconnect.Activities.MainActivity
import com.johanespino.familyconnect.R


class MapFragment : Fragment() {
    private lateinit var lat: TextView
    private lateinit var log: TextView
    // que es el permission ID?
    private val PERMISSION_ID=42;
    private lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        return view
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
}