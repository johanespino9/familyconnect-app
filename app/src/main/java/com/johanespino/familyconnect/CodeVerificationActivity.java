package com.johanespino.familyconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CodeVerificationActivity extends AppCompatActivity {
    EditText txtcode;
    FloatingActionButton flbtn;
    ImageButton btndelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
        txtcode = findViewById(R.id.verificationcode);
        flbtn = findViewById(R.id.NextBtn);
        btndelete = findViewById(R.id.btnlimpiarcodigo);

        flbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupid = txtcode.getText().toString();
                savePreferencesGroup(groupid);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtcode.setText("");
            }
        });
    }

    private void savePreferencesGroup(String timeStamp) {
        SharedPreferences preferences = getSharedPreferences("groupcredenciales", Context.MODE_PRIVATE);
        String groupid = timeStamp;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("groupId", groupid);
        editor.apply();
        Intent intent = new Intent(CodeVerificationActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}