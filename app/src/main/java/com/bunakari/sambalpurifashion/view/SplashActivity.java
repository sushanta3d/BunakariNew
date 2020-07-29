package com.bunakari.sambalpurifashion.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.GetPrefs;


public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        mobile = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");

     new Handler().postDelayed(() -> {
            if (mobile.length() == 0) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("pos","0");
                intent.putExtra("msg","");
                startActivity(intent);
                finish();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(GetPrefs.PREFS_SESSION,"");
                editor.commit();
            }
        },3000);
        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else { // For Below API 21
            // Implement this feature without material design
        }
    }



}
