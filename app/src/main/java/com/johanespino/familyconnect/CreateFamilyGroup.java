package com.johanespino.familyconnect;

import android.app.ActionBar;
import android.app.ProgressDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateFamilyGroup extends Fragment {
EditText email_pariente;
ImageButton btnAgregar;
    FirebaseFirestore db;

    public CreateFamilyGroup() {

    }

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_create_family_group, container, false);
        email_pariente=v.findViewById(R.id.email_pariente);
        db=FirebaseFirestore.getInstance();
        btnAgregar=v.findViewById(R.id.btnagregarpariente);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_pariente.getText().toString().trim();
                uploadData(email);
            }
        });
        return v;
    }

    private void uploadData(String email) {
        String id= UUID.randomUUID().toString();
        Map<String,Object> doc=new HashMap<>();
        doc.put("id",id);
        doc.put("email",email);

        db.collection("users").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}