package com.bunakari.sambalpurifashion.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.LoginResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.google.android.material.textfield.TextInputEditText;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private ProgressBar progressBar;
    static TextInputEditText dobEditText;
    private TextInputEditText nameEditText,emailEditText,mobEditText,pswdEditText,add1EditText,add2EditText,
            stateEditText,pincodeEditText,refIdEditText;
    private SharedPreferences sharedPreferences;
    private String mobileString;
    public static SignUpActivity signUpActivity;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude,longitude,city,state;
    Geocoder geocoder;
    double latti,longi;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
    }

    private void initUi() {

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        mobEditText = findViewById(R.id.mobEditText);
        pswdEditText = findViewById(R.id.pswdEditText);
        // dobEditText = findViewById(R.id.dobEditText);
        // add1EditText = findViewById(R.id.add1EditText);
       //  add2EditText = findViewById(R.id.add2EditText);
      //   stateEditText = findViewById(R.id.stateEditText);
      //   pincodeEditText = findViewById(R.id.pincodeEditText);
         refIdEditText = findViewById(R.id.refralEditText);
         signUpButton = findViewById(R.id.registerButton);
          progressBar = findViewById(R.id.progressBar);
          signUpButton.setOnClickListener(this);
      //  dobEditText.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        mobileString = getIntent().getStringExtra("mobile");

     //   mobEditText.setText(mobileString);


        signUpActivity = SignUpActivity.this;


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latti,longi,1);

                 city = addresses.get(0).getLocality();
                 state = addresses.get(0).getAdminArea();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public boolean isValidData(){
        int count = 0;

        if (nameEditText.getText().length() != 0){
            count++;
        }else {
            nameEditText.setError("Required Field");
        }

        if (emailEditText.getText().length() != 0){
            if (isValidEmail(emailEditText.getText().toString())) {
                count++;
            }else {
                emailEditText.setError("Please enter a valid email");
            }
        }else {
            emailEditText.setError("Required Field");
        }

        if (pswdEditText.getText().length() != 0){
            if (pswdEditText.getText().length() >= 6) {
                count++;
            }else {
                pswdEditText.setError("Please enter greater than 6 letter character");
            }
        }else {
            pswdEditText.setError("Required Field");
        }

        if (mobEditText.getText().length() != 0){
            if (mobEditText.getText().length() == 10) {
                count++;
            }else {
                mobEditText.setError("Please enter a valid mobile number");
            }
        }else {
            mobEditText.setError("Required Field");
        }






        if (count == 4){
            return true;
        }else {
            return false;
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @SuppressLint("ValidFragment")
    public static class DatePickerFragment extends androidx.fragment.app.DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            // Do something with the date chosen by the user
            //datePicker.setMaxDate(System.currentTimeMillis());
            month = month + 1;

            dobEditText.setText("" + day + "-" + "" + month + "-" + "" + year);

        }
    }

    private void RegisterUser() {
        ApiService apiService = RetroClass.getApiService();
        Call<LoginResponse> signResponse = apiService.signUp(nameEditText.getText().toString(),emailEditText.getText().toString(),mobEditText.getText().toString()
                ,city,state,refIdEditText.getText().toString(),pswdEditText.getText().toString());

        signResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                LoginResponse signupModel = response.body();
                try {
                    if (signupModel != null) {
                        if (signupModel.success == 1) {
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            intent.putExtra("pos","0");
                            startActivity(intent);
                            finish();
                            LoginActivity.loginActivity.finish();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(GetPrefs.PREFS_UNAME,nameEditText.getText().toString());
                            editor.putString(GetPrefs.PREFS_EMAIL,emailEditText.getText().toString());
                            editor.putString(GetPrefs.PREFS_MOBILE,mobEditText.getText().toString());
                            editor.putString(GetPrefs.PREFS_DOB," ");
                           editor.putString(GetPrefs.PREFS_ADDRESS,city);
                           editor.putString(GetPrefs.PREFS_STATE,state);
                           editor.putString(GetPrefs.PREFS_PINCODE," ");
                           editor.putString(GetPrefs.PREFS_UID,signupModel.userid);
                            editor.putString(GetPrefs.PREFS_APP_RFERRAL_CODE,signupModel.appreferralcode);
                            editor.commit();

                        }else if (signupModel.success == 3){
                            BasicFunction.showDailogBox(SignUpActivity.this,"Sorry,Mobile number already exist");
                        }else {
                            BasicFunction.showDailogBox(SignUpActivity.this,"Oops Something went wrong, Please try again..!!");
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    BasicFunction.showDailogBox(SignUpActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(SignUpActivity.this,"Oops Something went wrong, Please try again..!!");
                Log.d("ErrorResponse",t.getMessage());
            }
        });
    }
    
    @Override
    public void onClick(View view) {
      if (view.getId() == R.id.registerButton){
            if (isValidData()){
                if (BasicFunction.isNetworkAvailable(getApplicationContext())){
                    RegisterUser();
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    BasicFunction.showSnackbar(SignUpActivity.this,"No Internet Connection, Please try again..!!");
                }
            }
        }

      //else if (view.getId() == R.id.dobEditText){
    //        Log.d("inClick"," ");
     //       DialogFragment newFragment = new DatePickerFragment();
     //       newFragment.show(getSupportFragmentManager(),"datePicker");
      //  }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                latti = location.getLatitude();
                longi = location.getLongitude();
                //  lattitude = String.valueOf(latti);
                //  longitude = String.valueOf(longi);


                //    textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                //           + "\n" + "Longitude = " + longitude);

            } else  if (location1 != null) {
                latti = location1.getLatitude();
                longi = location1.getLongitude();
                //    lattitude = String.valueOf(latti);
                // longitude = String.valueOf(longi);

                //   textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                //         + "\n" + "Longitude = " + longitude);


            } else  if (location2 != null) {
                latti = location2.getLatitude();
                longi = location2.getLongitude();
                //  lattitude = String.valueOf(latti);
                //   longitude = String.valueOf(longi);

                //    textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
                //         + "\n" + "Longitude = " + longitude);

            }else{

                Toast.makeText(this,"Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }



    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    
}
