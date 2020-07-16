package com.johanespino.familyconnect

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class HomeActivity : AppCompatActivity() {
    private lateinit var lat:TextView
    private lateinit var log:TextView
    // que es el permission ID?
    val PERMISSION_ID=42;
    lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        lat = findViewById(R.id.tv_map_latitude)
        log = findViewById(R.id.tv_map_longitude)
        mFausedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getLastlocation();
    }
    @SuppressLint("MissingPermission")
    private fun getLastlocation() {
        if(checkPermission()){
            if(isLocationEnabled()){
                mFausedLocationProviderClient.lastLocation.addOnCompleteListener(this){task ->
                    var location: Location?= task.result
                    if(location == null){
                        requestNewLocationgData()
                    }else{
                        lat.text= location.latitude.toString();
                        log.text=location.longitude.toString();
                    }

                }
            }else{
                Toast.makeText(this,"Enciende tu ubicacion",Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }

    override fun onStart() {
        super.onStart()
        getLastlocation();
    }

    @SuppressLint( "MissingPermission")
    private  fun requestNewLocationgData(){
        var mLocationRequet = LocationRequest();
        mLocationRequet.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequet.interval=0
        mLocationRequet.fastestInterval=0
        mLocationRequet.numUpdates=1

        mFausedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        mFausedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequet,mLocationCallback,
            Looper.myLooper()
        )
    }

    private  val mLocationCallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var mLastLocation:Location=p0.lastLocation
            lat.text= mLastLocation.latitude.toString();
            log.text=mLastLocation.longitude.toString();
        }
    }

    private fun isLocationEnabled():Boolean{
        var locationManger: LocationManager = getSystemService(Context.LOCATION_SERVICE)as LocationManager
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManger.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private  fun checkPermission():Boolean{
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
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
}