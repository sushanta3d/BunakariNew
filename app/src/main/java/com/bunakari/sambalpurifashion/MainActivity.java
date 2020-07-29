package com.bunakari.sambalpurifashion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bunakari.sambalpurifashion.view.HomeActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

            startActivity(intent);
            finish();

        },3000);
    }
}
