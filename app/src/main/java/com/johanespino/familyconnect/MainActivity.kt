package com.johanespino.familyconnect

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    auth = Firebase.auth

    var btnLogin = findViewById<Button>(R.id.btnLogin)
    var txtEmail: EditText = findViewById(R.id.email) as EditText
    var txtPassword: EditText = findViewById(R.id.password) as EditText

    btnLogin.setOnClickListener { view ->
      signIn(view, txtEmail.text.toString(), txtPassword.text.toString())
    }
  }

  public override fun onStart() {
    super.onStart()

  }


  fun signIn(view: View, email: String, password: String) {

    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          Log.d("SIGIN", "signInWithEmail:success")

        } else {
          Log.w("SIGIN", "signInWithEmail:failure", task.exception)
          Toast.makeText(baseContext, "Authentication failed.",
            Toast.LENGTH_SHORT).show()
        }
      }
  }
}