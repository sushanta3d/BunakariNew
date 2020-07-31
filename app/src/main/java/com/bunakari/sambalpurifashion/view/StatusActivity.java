package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.GetPrefs;


public class StatusActivity extends AppCompatActivity {

    private TextView amountTextView,titleTextView,subTitleTextView;
    private Button homeButton;
    private String amountString,from;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initUi();

    }

    private void initUi() {
        amountTextView = findViewById(R.id.amountTextView);
        titleTextView = findViewById(R.id.titleTextView);
        subTitleTextView = findViewById(R.id.subTitleTextView);
        homeButton = findViewById(R.id.actionButton);

        from = getIntent().getStringExtra("from");
        amountString = getIntent().getStringExtra("amount");
        amountTextView.setText("\u20B9 "+amountString);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);

        if (from.equalsIgnoreCase("1")){
            homeButton.setVisibility(View.GONE);
            titleTextView.setText("Recharge Summary");
            subTitleTextView.setText("Money Added Successfully to Wallet");
        }

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                intent.putExtra("pos","0");
                intent.putExtra("msg","");
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        if (from.equalsIgnoreCase("1")){
            super.onBackPressed();
        }
    }
}
