package com.johanespino.familyconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class GroupAddActivity extends AppCompatActivity {
    FloatingActionButton btn;
    EditText etxttitle;
    FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_add);

        btn = findViewById(R.id.createGroupBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        etxttitle = findViewById(R.id.tittlegroup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String g_timeStamp = "" + System.currentTimeMillis();
                final String grouptit = etxttitle.getText().toString().trim();
                creategroup(g_timeStamp, grouptit);
                Intent intent = new Intent(GroupAddActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void creategroup(final String g_timeStamp, String grouptit) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("groupId", "" + g_timeStamp);
        hashMap.put("groupTitle", "" + grouptit);
        hashMap.put("createdBy", "" + firebaseAuth.getUid());
        db.collection("groups").document(g_timeStamp).set(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        HashMap<String, String> hashMap1 = new HashMap<>();
                        hashMap1.put("uid", firebaseAuth.getUid());
                        hashMap1.put("role", "admin");
                        hashMap1.put("timeStamp", g_timeStamp);
                        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                        db1.collection("groups").document(g_timeStamp).collection("participants").document(firebaseAuth.getUid()).set(hashMap1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(GroupAddActivity.this, "Grupo creado", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(GroupAddActivity.this, "Error al añadir administrador", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupAddActivity.this, "Grupo no creado", Toast.LENGTH_SHORT).show();
            }
        });

    }

//crear activity para añadir participantes

}