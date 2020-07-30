package com.johanespino.familyconnect

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
//  val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val btnLogin = findViewById<Button>(R.id.btn_login_sign_in)
        val btnRegister = findViewById<Button>(R.id.btn_login_register)
        val txtEmail: EditText = findViewById(R.id.et_login_email) as EditText
        val txtPassword: EditText = findViewById(R.id.et_login_password) as EditText

        btnLogin.setOnClickListener { view ->
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            //Comprobacion de campos
            if (!email.isEmpty() && !password.isEmpty()) {
                signIn(view, email, password)

            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
            }


        }

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun redirectActivity() {
//    val editText = findViewById<EditText>(R.id.editText)
//    val message = editText.text.toString()


        //añadir para listar usuarios pero para el participante
        //realizar consulta a la coleccion de grupos luego intentar una consulta
        //Buscar el metodo para sharedpreferences en fragment
        val preferences: SharedPreferences = getSharedPreferences("groupcredenciales", Context.MODE_PRIVATE)
        val groupid = preferences.getString("groupId", "No existe la informacion")
        if ("No existe la informacion" == groupid||groupid.isNullOrBlank()) {
            val intent = Intent(this, CodeVerificationActivity::class.java);
            startActivity(intent)

        } else {
            val intent = Intent(this, HomeActivity::class.java).apply {
//      putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }

    }

    private fun registerUser() {
        val intent = Intent(this, UserRegisterActivity::class.java).apply {
//      putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()

    }

    private fun signIn(view: View, email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Bienvenido $email", Toast.LENGTH_SHORT).show()
                    redirectActivity()

                    finish()
                } else {
                    Toast.makeText(baseContext, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                }
            }


    }
}