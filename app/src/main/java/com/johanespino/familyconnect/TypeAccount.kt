package com.johanespino.familyconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TypeAccount : AppCompatActivity() {
    lateinit var btnfree: Button
    lateinit var btngold: Button
    lateinit var btnblack: Button
    lateinit var txtomitir: TextView
    lateinit var txtomitir2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_account)
        //Inicializar vistas
        btnfree = findViewById(R.id.btnfree)
        btnblack = findViewById(R.id.btnblack)
        btngold = findViewById(R.id.btngold)
        txtomitir = findViewById(R.id.txtomitir1)
        txtomitir2 = findViewById(R.id.txtomitir2)

        //Planes de la aplicacion
        btnfree.setOnClickListener { v: View? ->
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btngold.setOnClickListener { v: View? ->
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        btnblack.setOnClickListener { v: View? ->
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}