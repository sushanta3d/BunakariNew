package com.bunakari.sambalpurifashion.view;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.CustomSwipeAdapter2;
import com.bunakari.sambalpurifashion.model.GetPrefs;

import java.util.ArrayList;


public class FullscreenActivity extends Activity {
    ArrayList<String> image_url = new ArrayList<>();
    ViewPager viewPager;
    CustomSwipeAdapter2 swipeAdapter;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        super.setRequestedOrientation(1);

        viewPager = findViewById(R.id.viewpager);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);

        Intent i = getIntent();
        int position = i.getIntExtra("pos", 0);
        this.image_url = i.getStringArrayListExtra("image");

        swipeAdapter = new CustomSwipeAdapter2(FullscreenActivity.this,getApplicationContext(),image_url);
        viewPager.setAdapter(swipeAdapter);

        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String loginSession = sharedPreferences.getString(GetPrefs.PREFS_SESSION,"");
        if (loginSession.equalsIgnoreCase("2")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GetPrefs.PREFS_SESSION,"1");
            editor.commit();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            //called when app goes to background
            String loginSession = sharedPreferences.getString(GetPrefs.PREFS_SESSION,"");
            if (loginSession.equalsIgnoreCase("1")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(GetPrefs.PREFS_SESSION,"2");
                editor.commit();
            }
        }
    }

}
