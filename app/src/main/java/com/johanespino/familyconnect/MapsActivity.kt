package com.johanespino.familyconnect

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var dataBase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var hisUid: String? = null
    var myUid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseFirestore.getInstance()
        val intent = intent
        hisUid = intent.getStringExtra("SuUID")
        Toast.makeText(this, "${hisUid}", Toast.LENGTH_LONG).show()
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

        var a = auth.currentUser



        val docRef = hisUid?.let { dataBase.collection("users").document(it) }
        docRef?.get()
            ?.addOnSuccessListener { document ->



                val user = a?.uid?.let { dataBase.collection("users").document(it) }

                user?.get()?.addOnSuccessListener { user ->


                if (document != null) {
                    val myMarker = LatLng(user.getString("lat")!!.toDouble(), user.getString("lng")!!.toDouble())
                    val otherMarker = LatLng(document.getString("lat")!!.toDouble(),document.getString("lng")!!.toDouble())
                    val zoomLevel = 16.0f

                    var polyline1: Polyline = googleMap.addPolyline( PolylineOptions()
                            .clickable(true)
                        .add(myMarker, otherMarker))

                    polyline1.tag = "A"

                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myMarker, zoomLevel))
                    var im = map?.addMarker(MarkerOptions().position(myMarker).title("Yo"))
                    var friend = map?.addMarker(MarkerOptions().position(otherMarker).title("Otro usuario"))

                    im.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_foreground))
                    friend.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_foreground))
                    map?.moveCamera(CameraUpdateFactory.newLatLng(myMarker))
                } else {
                    Log.d("a", "No such document")
                }

                }
            }
            ?.addOnFailureListener { exception ->
                Log.d("a", "get failed with ", exception)
            }






    }
}