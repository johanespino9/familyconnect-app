package com.johanespino.familyconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.johanespino.familyconnect.Activities.HomeActivity;

public class SplashTheme extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
