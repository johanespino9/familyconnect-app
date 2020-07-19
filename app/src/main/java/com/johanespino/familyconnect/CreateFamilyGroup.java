package com.johanespino.familyconnect;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateFamilyGroup extends Fragment {
    EditText email_pariente;
    ImageButton btnAgregar;
    FirebaseFirestore db;
    //Instancia de Firebase
    private FirebaseAuth mAuth;

    public CreateFamilyGroup() {

    }

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_family_group, container, false);
        email_pariente = v.findViewById(R.id.email_pariente);
        db = FirebaseFirestore.getInstance();
        //Obtener instancia
        mAuth = FirebaseAuth.getInstance();
        btnAgregar = v.findViewById(R.id.btnagregarpariente);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_pariente.getText().toString().trim();
                uploadData(email);
            }
        });
        return v;
    }

    private void uploadData(final String email) {

        //Registrar usuario
        // final String id= UUID.randomUUID().toString();
        final String password = "12345678";
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String id = user.getUid();

                    //crear objeto para subir
                    Map<String, Object> doc = new HashMap<>();
                    doc.put("id", id);
                    doc.put("email", email);

                    //subir a firestore
                    db.collection("users").document(id).set(doc)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Envio fallido", Toast.LENGTH_SHORT).show(); } }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }});
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(getContext(), "Registrado\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                    // Una vez receptemos el evento, usaremos Bundle e Intent para pasar datos de una Activity a otra

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getContext(), "Autenticacion fallida", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}