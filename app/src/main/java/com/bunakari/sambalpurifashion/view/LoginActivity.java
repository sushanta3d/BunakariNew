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
import com.bunakari.sambalpurifashion.model.OtpResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.google.android.material.textfield.TextInputEditText;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView forgotTextView,signUpTextView;
    Button loginButton;
    TextInputEditText mobEditText,pswdEditText;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    public static LoginActivity loginActivity;
    String mobile,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
    }

    private void initUi() {

        forgotTextView = findViewById(R.id.forgotTextView);
        signUpTextView = findViewById(R.id.signUpTextView);

        loginButton = findViewById(R.id.loginButton);
        mobEditText = findViewById(R.id.mobEditText);
        pswdEditText = findViewById(R.id.pswdEditText);
        progressBar = findViewById(R.id.progressBar);

        forgotTextView.setOnClickListener(this);
        signUpTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        loginActivity = LoginActivity.this;
    }

    public boolean isValidData(){
        int count = 0;

        if (mobEditText.getText().length() != 0){
            if (mobEditText.getText().length() == 10) {
                count++;
            }else {
                mobEditText.setError("Please enter a valid mobile number");
            }
        }else {
            mobEditText.setError("Required Field");
        }

        if (count == 1){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.signUpTextView){
            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.forgotTextView){

        }else if (view.getId() == R.id.loginButton){
            if (isValidData()){
                if (BasicFunction.isNetworkAvailable(getApplicationContext())){
                    mobile=mobEditText.getText().toString();
                    password=pswdEditText.getText().toString();
               //    GetOtp();
                    GetLogin();
                }else {
                    BasicFunction.showSnackbar(LoginActivity.this,"No Internet Connection, Please try again..!!");
                }
            }
        }
    }
    private void GetLogin() {
        ApiService apiService = RetroClass.getApiService();
        Call<LoginResponse> signResponse = apiService.login(mobile,password);

        signResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                LoginResponse signupModel = response.body();
                Log.d("SignUpResponse",signupModel+" \n "+response.body());
                try {
                    if (signupModel != null) {
                        if (signupModel.success == 1) {
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.putExtra("pos","0");
                            intent.putExtra("msg","");
                            startActivity(intent);
                            finish();
                            LoginActivity.loginActivity.finish();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(GetPrefs.PREFS_UNAME,signupModel.name);
                            editor.putString(GetPrefs.PREFS_EMAIL,signupModel.email);
                            editor.putString(GetPrefs.PREFS_MOBILE,signupModel.mob);
                            editor.putString(GetPrefs.PREFS_DOB,signupModel.dob);
                            editor.putString(GetPrefs.PREFS_ADDRESS,signupModel.address);
                            editor.putString(GetPrefs.PREFS_STATE,signupModel.state);
                            editor.putString(GetPrefs.PREFS_PINCODE,signupModel.pincode);
                            editor.putString(GetPrefs.PREFS_UID,signupModel.userid);
                            editor.putString(GetPrefs.PREFS_APP_RFERRAL_CODE,signupModel.appreferralcode);
                            editor.commit();

                        }else if (signupModel.success == 2){
                            Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);
                            finish();
                        }else {
                            BasicFunction.showDailogBox(LoginActivity.this,"Oops Something went wrong, Please try again..!!");
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    BasicFunction.showDailogBox(LoginActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(LoginActivity.this,"Oops Something went wrong, Please try again..!!");
                Log.d("ErrorResponse",t.getMessage());
            }
        });
    }
    private void GetOtp() {
        ApiService apiService = RetroClass.getApiService();
        Call<OtpResponse> signResponse = apiService.getOtp(mobEditText.getText().toString());

        progressBar.setVisibility(View.VISIBLE);
        signResponse.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                progressBar.setVisibility(View.GONE);
                OtpResponse otpResponse = response.body();
                Log.d("SignUpResponse", otpResponse.message.content + " \n " + response.body());
                try {
                    if (otpResponse != null) {
                        if (otpResponse.status.equalsIgnoreCase("success")) {
                            Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                            intent.putExtra("votp", otpResponse.message.content);
                            intent.putExtra("mobile", mobEditText.getText().toString());
                            startActivity(intent);
                        } else {
                            BasicFunction.showDailogBox(LoginActivity.this, "Oops Something went wrong, Please try again..!!");
                        }
                    } else {
                        BasicFunction.showDailogBox(LoginActivity.this, "Oops Something went wrong, Please try again..!!");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    BasicFunction.showDailogBox(LoginActivity.this, "Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(LoginActivity.this, "Oops Something went wrong, Please try again..!!");
                Log.d("ErrorResponse", t.getMessage());
            }
        });
    }
}
