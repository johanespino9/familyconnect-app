package com.johanespino.familyconnect

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth
  val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    auth = Firebase.auth

    val btnLogin = findViewById<Button>(R.id.btnLogin)
    val btnRegister = findViewById<Button>(R.id.btnRegister)
    val txtEmail: EditText = findViewById(R.id.email) as EditText
    val txtPassword: EditText = findViewById(R.id.password) as EditText

    btnLogin.setOnClickListener { view ->
      signIn(view, txtEmail.text.toString(), txtPassword.text.toString())
    }

    btnRegister.setOnClickListener { view ->
      registerUser()
    }
  }

  fun redirectActivity() {
//    val editText = findViewById<EditText>(R.id.editText)
//    val message = editText.text.toString()

    val intent = Intent(this, HomeActivity::class.java).apply {
//      putExtra(EXTRA_MESSAGE, message)
    }
    startActivity(intent)
  }

  fun registerUser(){
    val intent = Intent(this, UserRegisterActivity::class.java).apply {
//      putExtra(EXTRA_MESSAGE, message)
    }
    startActivity(intent)
  }

  public override fun onStart() {
    super.onStart()

  }


  fun signIn(view: View, email: String, password: String) {

    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          Log.d("SIGIN", "signInWithEmail:success")
          redirectActivity()
          finish()
        } else {
          Log.w("SIGIN", "signInWithEmail:failure", task.exception)
          Toast.makeText(baseContext, "Authentication failed.",
            Toast.LENGTH_SHORT).show()
        }
      }
  }
}