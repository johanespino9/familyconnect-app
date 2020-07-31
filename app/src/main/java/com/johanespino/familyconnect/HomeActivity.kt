package com.johanespino.familyconnect


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
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
        checkUserStatus()

//        fragment = BuscarMascotaFragment()
//        (getActivity() as Main2Activity).loadFragment(fragment)

//BottomNavigation
        val navigationView = findViewById<BottomNavigationView>(R.id.btn_nav)
        navigationView.setOnNavigationItemSelectedListener(selectedListener)
//home default
// home default

        // Obtienes el Bundle del Intent
        //  val bundle = intent.extras
        // Obtienes el texto
        // val timeStamp = bundle!!.getString("timeStamp")
        // Creamos un nuevo Bundle
        //val args = Bundle()
        // Colocamos el String
        //args.putString("timeStamp", timeStamp)
        // Supongamos que tu Fragment se llama TestFragment. Colocamos este nuevo Bundle como argumento en el fragmento.
        val fragment1 = MapFragment()
        //fragment1.setArguments(args)
        val ft1 = supportFragmentManager.beginTransaction()
        ft1.replace(R.id.content, fragment1, "")
        ft1.commit()


    }

    //Revisar el estado del usuario
    private fun checkUserStatus() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Toast.makeText(this, "En sesion: " + user.email, Toast.LENGTH_LONG).show();
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

    private val selectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem -> //handle
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    //profile fragment transaction
                    val fragment2 =
                        PerfilUsuario()
                    val ft2 = supportFragmentManager.beginTransaction()
                    ft2.replace(R.id.content, fragment2, "")
                    ft2.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_locate -> {
                    //home fragment transaction
                    val fragment1 =
                        ListUserFragment()
                    val ft1 = supportFragmentManager.beginTransaction()
                    ft1.replace(R.id.content, fragment1, "")
                    ft1.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_mapa -> {
                    val fragment4 =
                        MapFragment()
                    val ft4 = supportFragmentManager.beginTransaction()
                    ft4.replace(R.id.content, fragment4, "")
                    ft4.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_chat -> {
                    val fragment5 =
                        ListUserFragment()
                    val ft5 = supportFragmentManager.beginTransaction()
                    ft5.replace(R.id.content, fragment5, "")
                    ft5.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_alerts -> {
                    //users fragment transaction
                    val fragment3 =
                        MapFragment()
                    val ft3 = supportFragmentManager.beginTransaction()
                    ft3.replace(R.id.content, fragment3, "")
                    ft3.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
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