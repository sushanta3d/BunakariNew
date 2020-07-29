package com.bunakari.sambalpurifashion.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class BusinessRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText refIdEditText,adhaarNoEditText;
    private Button registerButton;
    private TextView signInTextView,fillRefIdTextView;
    private Spinner depositSpinner;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String uidString,spinnerString = "";
    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);

        initUi();

    }

    private void initUi() {
        refIdEditText = findViewById(R.id.refIdEditText);
        adhaarNoEditText = findViewById(R.id.adhaarIdEditText);
        registerButton = findViewById(R.id.registerButton);
        signInTextView = findViewById(R.id.signInTextView);
        fillRefIdTextView = findViewById(R.id.fillRefIdTextView);
        depositSpinner = findViewById(R.id.depositSpinner);
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        registerButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
        fillRefIdTextView.setOnClickListener(this);

        String[] depositArray = getResources().getStringArray(R.array.deposit_item);

        final ArrayAdapter<String> madeInspinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, depositArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        madeInspinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        depositSpinner.setAdapter(madeInspinnerArrayAdapter);

        depositSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    spinnerString = selectedItemText;
                }else {
                    spinnerString = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void RegisterBusiness(){
        ApiService registerService = RetroClass.getApiService();
        Call<LoginResponse> registerResponseCall = registerService.registerBusiness(uidString,refIdEditText.getText().toString(),adhaarNoEditText.getText().toString(),spinnerString);
        registerResponseCall.enqueue(new Callback<LoginResponse>() {
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
                        BusinessLoginActivity.businessLoginActivity.finish();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(GetPrefs.PREFS_RFERRAL_ID,loginResponse.referalid);
                        editor.putString(GetPrefs.PREFS_DEPOSIT,spinnerString);
                        editor.putString(GetPrefs.PREFS_SESSION,"1");
                        editor.commit();

                    }else if (loginResponse.success == 4){
                        BasicFunction.showDailogBox(BusinessRegisterActivity.this,"Sorry, This referral id usage limit is exceed, Please try another referral id");
                    }else {
                        BasicFunction.showDailogBox(BusinessRegisterActivity.this,"Oops Something went wrong, Please try again..!!");
                    }
                }else {
                    BasicFunction.showDailogBox(BusinessRegisterActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(BusinessRegisterActivity.this,"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.registerButton){
            if (refIdEditText.getText().length() != 0){
                if (spinnerString.length() != 0) {
                    Intent intent = new Intent(getApplicationContext(),checksum.class);
                    intent.putExtra("orderid", String.valueOf(getID()));
                    intent.putExtra("amount",spinnerString);
                    startActivityForResult(intent,3);
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Please Select Deposit Amount...");
                }
            }else {
                refIdEditText.setError("Required Field..!!");
            }
        }else if (view.getId() == R.id.signInTextView){
            finish();
        }else if (view.getId() == R.id.fillRefIdTextView){
            if (refIdEditText.getText().toString().equalsIgnoreCase("0000FIM0")){

            }else {
                refIdEditText.setText("0000FIM0");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 3) {
            if (resultCode == 2) {
                int position = data.getIntExtra("status",0);
                if (position == 1) {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        RegisterBusiness();
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        BasicFunction.showSnackbar(BusinessRegisterActivity.this, "No internet connection,Please try again..!!");
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
