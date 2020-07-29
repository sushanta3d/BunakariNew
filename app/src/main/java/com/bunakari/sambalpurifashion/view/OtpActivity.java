package com.bunakari.sambalpurifashion.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.LoginResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText num1EditText,num2EditText,num3EditText,num4EditText;

    private TextView resendTextView;

    private Button verifyButton;

    private ProgressBar progressBar;
    private String votp,name,email,mobile,pswd,dob,add1,add2,state,pincode,appReferralCode;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        initUi();

    }

    private void initUi() {
        num1EditText = findViewById(R.id.num1EditText);
        num2EditText = findViewById(R.id.num2EditText);
        num3EditText = findViewById(R.id.num3EditText);
        num4EditText = findViewById(R.id.num4EditText);

        verifyButton = findViewById(R.id.verifyButton);
        resendTextView = findViewById(R.id.resendTextView);
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);

        votp = getIntent().getStringExtra("votp");
        mobile = getIntent().getStringExtra("mobile");

        votp = votp.substring(0,4);

        num1EditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num1EditText.setSelection(num1EditText.getText().length());
                // TODO Auto-generated method stub
                if (num1EditText.getText().toString().length() == 1)     //size as per your requirement
                {
                    num2EditText.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        num2EditText.addTextChangedListener(new TextWatcher() {


            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num2EditText.setSelection(num2EditText.getText().length());
                // TODO Auto-generated method stub

                if (num2EditText.getText().toString().length() == 1)     //size as per your requirement
                {
                    num3EditText.requestFocus();
                }

                if (num2EditText.getText().toString().equalsIgnoreCase("")) {
                    Log.d("innm2", " 08");
                    num1EditText.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        num3EditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num3EditText.setSelection(num3EditText.getText().length());
                // TODO Auto-generated method stub

                if (num3EditText.getText().toString().length() == 1)     //size as per your requirement
                {
                    num4EditText.requestFocus();
                }

                if (num3EditText.getText().toString().equalsIgnoreCase("")) {
                    Log.d("innm3", " 07");
                    num2EditText.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        num4EditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num3EditText.setSelection(num3EditText.getText().length());
                // TODO Auto-generated method stub

                if (num4EditText.getText().toString().length() == 1)     //size as per your requirement
                {
                    num4EditText.setSelection(num4EditText.getText().length());
                }

                if (num4EditText.getText().toString().equalsIgnoreCase("")) {
                    Log.d("innm4", " 02");
                    num3EditText.requestFocus();
                }
                verify();
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        resendTextView.setOnClickListener(this::onClick);
        verifyButton.setOnClickListener(this::onClick);

    }

    public void verify() {

        char first = votp.charAt(0);
        Log.d("mess1",first+" 099");
        char second = votp.charAt(1);
        Log.d("mess2",second+" 099");
        char third = votp.charAt(2);
        Log.d("mess3",third+" 099");
        char fourth = votp.charAt(3);
        Log.d("mess4",fourth+" 099");

        if (num1EditText.getText().toString().trim().equals(String.valueOf(first).trim()) && num2EditText.getText().toString().trim().equals(String.valueOf(second).trim()) && num3EditText.getText().toString().trim().equals(String.valueOf(third).trim()) && num4EditText.getText().toString().trim().equals(String.valueOf(fourth).trim())) {
            Log.d("Success"," 0908");
            Toast.makeText(getApplicationContext(), "Your Mobile No. Verified Successfully !!!", Toast.LENGTH_LONG).show();
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            checkUser();
            progressBar.setVisibility(View.VISIBLE);

        } else {
            num4EditText.setError("Enter Valid Otp");
        }
    }

    private void checkUser() {
        ApiService apiService = RetroClass.getApiService();
        Call<LoginResponse> signResponse = apiService.getCheckUser(mobile);

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
                            BasicFunction.showDailogBox(OtpActivity.this,"Oops Something went wrong, Please try again..!!");
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    BasicFunction.showDailogBox(OtpActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(OtpActivity.this,"Oops Something went wrong, Please try again..!!");
                Log.d("ErrorResponse",t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.resendTextView){
            finish();
        }else if (view.getId() == R.id.verifyButton){
            verify();
        }
    }
}
