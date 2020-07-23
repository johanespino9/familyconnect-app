package com.johanespino.familyconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class UserRegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    //    private lateinit var dataBase: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        auth = Firebase.auth
        val dataBase = FirebaseFirestore.getInstance()
        val etFirstName = findViewById<EditText>(R.id.et_first_name)
        val etLastName = findViewById<EditText>(R.id.et_last_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etRepeatedPassword = findViewById<EditText>(R.id.et_repeated_password)
        val btnNext = findViewById<Button>(R.id.btn_next)



        btnNext.setOnClickListener { view ->
            // TODO: Registrar con auth y agregar a base de datos firestore
            val firstName = etFirstName.text
            val lastName = etLastName.text
            val email = etEmail.text
            val password = etPassword.text
            val repeatedPassword = etRepeatedPassword.text

            if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !repeatedPassword.isEmpty()) {

                if (password.length >= 8) {
                    Log.d("LENGTH", "Password es mayor a 8 ")
                    registerUser(
                        email.toString(),
                        password.toString(),
                        firstName.toString(),
                        lastName.toString(),
                        dataBase
                    )
                    Toast.makeText(this, "Usuario Registrado correctamente", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        "La contraseña debe tener 8 ó más caraceteres",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun registerUser(

        email : String,
        password: String,
        firstName: String,
        lastName: String,
        database: FirebaseFirestore
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userToCreate = hashMapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "role" to "admin"
                    )

                    registerUserInDB(userToCreate, database)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("REGISTER", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
//                    updateUI(null)
                }

                // ...
            }
    }

    private fun registerUserInDB(userToCreate: HashMap<String, String>, database: FirebaseFirestore) {
        val loadingDialog = LoadingDialog(this@UserRegisterActivity)
        loadingDialog.startloadingAlertDialog()
        database.collection("users")
            .document(auth.uid.toString()).set(userToCreate)
            .addOnSuccessListener { documentReference ->
               loadingDialog.dismissLoadingAlertDialog()
                redirectActivity()

                finish()
            }
            .addOnFailureListener { e ->

            }
        // Sign in success, update UI with the signed-in user's information
        Log.d("REGISTER", "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
    }

    private fun redirectActivity() {
        val user = auth.currentUser
        val id = user!!.uid
//    val editText = findViewById<EditText>(R.id.editText)
//    val message = editText.text.toString()
        val intent = Intent(this, TypeAccount::class.java)
        val b: Bundle = Bundle()
        b.putString("uid", id)
        intent.putExtras(b)
        startActivity(intent)
    }
}