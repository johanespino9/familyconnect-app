package com.johanespino.familyconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUserGroup extends AppCompatActivity {
    EditText email_pariente;
    ImageButton btnAgregar;
    FloatingActionButton btnNext;
    FirebaseFirestore db;
    //Instancia de Firebase
    private FirebaseAuth mAuth;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_group);
        email_pariente = findViewById(R.id.email_pariente);
        db = FirebaseFirestore.getInstance();
        //Obtener instancia
        mAuth = FirebaseAuth.getInstance();
        Bundle parametros = this.getIntent().getExtras();
        timeStamp = parametros.getString("timeStamp");
        btnAgregar = findViewById(R.id.btnagregarpariente);
        btnNext = findViewById(R.id.NextBtn);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_pariente.getText().toString().trim();
                uploadData(email);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUserGroup.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadData(final String email) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        //Registrar usuario
        // final String id= UUID.randomUUID().toString();
        final String password = "12345678";
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    String uid = user.getUid();
                    //crear objeto para subir
                    Map<String, Object> doc = new HashMap<>();
                    doc.put("uid", uid);
                    doc.put("firstName", "");
                    doc.put("lastName", "");
                    doc.put("email", email);
                    doc.put("role", "participante");
                    doc.put("imagen", "");

                    //subir a firestore
                    db.collection("users").document(uid).set(doc)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                    hashMap1.put("uid", auth.getUid());
                                    hashMap1.put("role", "participante");
                                    hashMap1.put("timeStamp", timeStamp);
                                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                                    db1.collection("groups").document(timeStamp).collection("participants").
                                            document(auth.getUid()).set(hashMap1)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AddUserGroup.this, "Usuario agregado al grupo", Toast.LENGTH_SHORT).show();
                                                    cargarCredenciales();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddUserGroup.this, "Error al añadir usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddUserGroup.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(AddUserGroup.this, "Envio fallido", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AddUserGroup.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddUserGroup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(AddUserGroup.this, "Registrado\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                    // Una vez receptemos el evento, usaremos Bundle e Intent para pasar datos de una Activity a otra

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(AddUserGroup.this, "Autenticacion fallida", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void redirectActivity(String g_timeStamp) {
        // String timeStamp=g_timeStamp;
        Bundle bundle = new Bundle();
        // Inicializas el Intent
        Intent intent = new Intent(AddUserGroup.this, HomeActivity.class);
        // Información del EditText
        String timeStamp = g_timeStamp;
        // Agregas la información del EditText al Bundle
        bundle.putString("timeStamp", timeStamp);
        // Agregas el Bundle al Intent e inicias ActivityB
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //vuelve al usuario original
    private void cargarCredenciales() {
        final FirebaseAuth auth2 = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String correo = preferences.getString("email", "No existe la informacion");
        String password = preferences.getString("password", "No existe la informacion");
        auth2.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}