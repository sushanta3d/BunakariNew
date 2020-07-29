package com.bunakari.sambalpurifashion.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.LoginResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.google.android.material.textfield.TextInputEditText;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText refIdEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String uidString;
    public static BusinessLoginActivity businessLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_login);

        initUi();

    }

    private void initUi() {
        refIdEditText = findViewById(R.id.refIdEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        progressBar = findViewById(R.id.progressBar);

        businessLoginActivity = BusinessLoginActivity.this;

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        loginButton.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
    }

    private void LoginBusiness(){
        ApiService loginService = RetroClass.getApiService();
        Call<LoginResponse> loginResponseCall = loginService.loginBusiness(uidString,refIdEditText.getText().toString());
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                LoginResponse loginResponse = response.body();
                if (loginResponse != null) {
                    if (loginResponse.success == 1){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("pos","5");
                        intent.putExtra("msg","");
                        startActivity(intent);
                        finish();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(GetPrefs.PREFS_RFERRAL_ID,loginResponse.referalid);
                        editor.putString(GetPrefs.PREFS_SESSION,"1");
                        editor.putString(GetPrefs.PREFS_DEPOSIT,loginResponse.dpamount);
                        editor.commit();

                    }else {
                        BasicFunction.showDailogBox(BusinessLoginActivity.this,"Oops Something went wrong, Please try again..!!");
                    }
                }else {
                    BasicFunction.showDailogBox(BusinessLoginActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("ErrorBusiness",t.getMessage()+" ");
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(BusinessLoginActivity.this,"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.loginButton){
            if (refIdEditText.getText().length() != 0){
                if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                    LoginBusiness();
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    BasicFunction.showSnackbar(BusinessLoginActivity.this,"No internet connection,Please try again..!!");
                }
            }else {
                refIdEditText.setError("Required Field..!!");
            }
        }else if (view.getId() == R.id.signUpTextView){
            Intent intent = new Intent(getApplicationContext(),BusinessRegisterActivity.class);
            startActivity(intent);
        }
    }
}
