package com.johanespino.familyconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypePlan extends AppCompatActivity {
Button btnfree,btngold,btnblack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_plan);
        btnfree=findViewById(R.id.btnfree_f);
        btngold=findViewById(R.id.btngold_f);
        btnblack=findViewById(R.id.btnblack_f);
        btnfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypePlan.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        btngold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypePlan.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        btnblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypePlan.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}