package com.johanespino.familyconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun checkUserStatus(){
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            //Stay here if user is signed
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
    }}

    public override fun onStart() {
        checkUserStatus()
        super.onStart()

    }
}