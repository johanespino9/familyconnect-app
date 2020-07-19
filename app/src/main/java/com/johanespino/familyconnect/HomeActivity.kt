package com.johanespino.familyconnect


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var lat: TextView
    private lateinit var log: TextView

    // que es el permission ID?
    private val PERMISSION_ID = 42;
    private lateinit var mFausedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//BottomNavigation
        val navigationView = findViewById<BottomNavigationView>(R.id.btn_nav)
        navigationView.setOnNavigationItemSelectedListener(selectedListener)
//home default
// home default
        val fragment1 = MapFragment()
        val ft1 = supportFragmentManager.beginTransaction()
        ft1.replace(R.id.content, fragment1, "")
        ft1.commit()

    }
    private val selectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem -> //handle
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    //home fragment transaction
                    val fragment1 = MapFragment()
                    val ft1 = supportFragmentManager.beginTransaction()
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    //profile fragment transaction
                    val fragment2 = MapFragment()
                    val ft2 = supportFragmentManager.beginTransaction()
                    ft2.replace(R.id.content, fragment2, "")
                    ft2.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_users -> {
                    //users fragment transaction
                    val fragment3 = MapFragment()
                    val ft3 = supportFragmentManager.beginTransaction()
                    ft3.replace(R.id.content, fragment3, "")
                    ft3.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_mapa -> {
                    val fragment4 = MapFragment()
                    val ft4 = supportFragmentManager.beginTransaction()
                    ft4.replace(R.id.content, fragment4, "")
                    ft4.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_chat -> {
                    val fragment5 = MapFragment()
                    val ft5 = supportFragmentManager.beginTransaction()
                    ft5.replace(R.id.content, fragment5, "")
                    ft5.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    //Revisar el estado del usuario
    private fun checkUserStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            //Si esta logeado se mantiene aqui
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        checkUserStatus()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        if (menu != null) {
            menu.findItem(R.id.action_post).isVisible = false
        }
        if (menu != null) {
            menu.findItem(R.id.action_search).isVisible = false
        }

        return true
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



}