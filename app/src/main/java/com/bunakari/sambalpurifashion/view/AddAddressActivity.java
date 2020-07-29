package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.AddressResponse;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText,mobileEditText,pincodeEditText,addressEditText,areaEditText,landmarkEditText,cityEditText,stateEditText;
    private TextView notAvailableTextView,addAddressTextView,officeTimeTextView;
    private ImageView homeImageView,officeImageView;
    private CheckBox satCheckBox,sunCheckBox;
    private ArrayList<AddressResponse> dataList = new ArrayList();
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String uidString,flag,time="0";
    private int position = 0;
    private RelativeLayout homeLayout,officeLayout,satSunLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initUi();

    }

    private void initUi() {
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        pincodeEditText = findViewById(R.id.pincodeEditText);
        addressEditText = findViewById(R.id.flatEditText);
        areaEditText = findViewById(R.id.areaEditText);
        landmarkEditText = findViewById(R.id.landmarkEditText);
        cityEditText = findViewById(R.id.cityEditText);
        stateEditText = findViewById(R.id.stateEditText);
        progressBar = findViewById(R.id.progressBar);
     /*   homeImageView = findViewById(R.id.homeImgView);
        officeImageView = findViewById(R.id.officeImgView);
        satCheckBox = findViewById(R.id.satCheckBox);
        sunCheckBox = findViewById(R.id.sunCheckBox);
        officeTimeTextView = findViewById(R.id.officeTimeTextView);


        homeLayout = findViewById(R.id.homeaddresslayout);
        officeLayout = findViewById(R.id.officeaddresslayout);
        satSunLayout = findViewById(R.id.satsunlayout);*/
        addAddressTextView = findViewById(R.id.deliverTextView);
        addAddressTextView.setOnClickListener(this);
     //   homeImageView.setOnClickListener(this);
       // officeImageView.setOnClickListener(this);
     //   satCheckBox.setOnClickListener(this);
      //  sunCheckBox.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        flag = getIntent().getStringExtra("flag");

        if (!flag.equalsIgnoreCase("1")){
            getSupportActionBar().setTitle("Edit Booking Address");
            dataList = (ArrayList) getIntent().getSerializableExtra("datalist");
            position = getIntent().getIntExtra("position",0);
            setViewData();
        }else {
            getSupportActionBar().setTitle("Add Booking Address");
        }
    }

    private void setViewData() {
        nameEditText.setText(dataList.get(position).getName());
        mobileEditText.setText(dataList.get(position).getMobile());
        pincodeEditText.setText(dataList.get(position).getPincode());
        addressEditText.setText(dataList.get(position).getAddress());
        areaEditText.setText(dataList.get(position).getArea());
        landmarkEditText.setText(dataList.get(position).getLandmark());
        cityEditText.setText(dataList.get(position).getCity());
        stateEditText.setText(dataList.get(position).getState());
        time = dataList.get(position).getDtime();

     /*   if (time.equalsIgnoreCase("1")){
            homeLayout.setBackgroundResource(R.drawable.blueborder);
            homeImageView.setImageResource(R.drawable.selectedradio);
        }else if (time.equalsIgnoreCase("2")){
            officeLayout.setBackgroundResource(R.drawable.blueborder);
            officeImageView.setImageResource(R.drawable.selectedradio);
        }else if (time.equalsIgnoreCase("3")){
            officeLayout.setBackgroundResource(R.drawable.blueborder);
            officeImageView.setImageResource(R.drawable.selectedradio);
            satSunLayout.setVisibility(View.VISIBLE);
            satCheckBox.setChecked(true);
        }else if (time.equalsIgnoreCase("4")){
            officeLayout.setBackgroundResource(R.drawable.blueborder);
            officeImageView.setImageResource(R.drawable.selectedradio);
            satSunLayout.setVisibility(View.VISIBLE);
            sunCheckBox.setChecked(true);
        }else if (time.equalsIgnoreCase("5")){
            officeLayout.setBackgroundResource(R.drawable.blueborder);
            officeImageView.setImageResource(R.drawable.selectedradio);
            satSunLayout.setVisibility(View.VISIBLE);
            satCheckBox.setChecked(true);
            sunCheckBox.setChecked(true);
        }*/
    }

    private boolean ValidateData(){
        int count = 0;

        if (nameEditText.getText().length() == 0){
            nameEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (mobileEditText.getText().length() == 0){
            mobileEditText.setError("Required Field..!!");
        }else {
            if (mobileEditText.getText().length() == 10) {
                count++;
            }else {
                mobileEditText.setError("Please enter a valid mobile number...");
            }
        }

        if (pincodeEditText.getText().length() == 0){
            pincodeEditText.setError("Required Field..!!");
        }else {
            if (pincodeEditText.getText().length() == 6) {
                count++;
            }else {
                pincodeEditText.setError("Please enter a valid pincode...");
            }
        }

        if (addressEditText.getText().length() == 0){
            addressEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (areaEditText.getText().length() == 0){
            areaEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (landmarkEditText.getText().length() == 0){
            landmarkEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (cityEditText.getText().length() == 0){
            cityEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (stateEditText.getText().length() == 0){
            stateEditText.setError("Required Field..!!");
        }else {
            count++;
        }

        if (count == 8){
            return true;
        }else {
            return false;
        }
    }

    private void AddAddress(){
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addAddress(uidString,nameEditText.getText().toString(),mobileEditText.getText().toString(),
                pincodeEditText.getText().toString(),addressEditText.getText().toString(),areaEditText.getText().toString(),landmarkEditText.getText().toString(),
                cityEditText.getText().toString(),stateEditText.getText().toString(),time);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        Intent intent = new Intent(getApplicationContext(),PaymentMethodActivity.class);
                        intent.putExtra("addressId",signupResponse.addressid);
                        startActivity(intent);
                        finish();
                    }else {
                        BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
                    }
                }else {
                    BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
            }
        });
    }

    private void UpdateAddress(){
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.updateAddress(dataList.get(position).getId(),uidString,nameEditText.getText().toString(),mobileEditText.getText().toString(),
                pincodeEditText.getText().toString(),addressEditText.getText().toString(),areaEditText.getText().toString(),landmarkEditText.getText().toString(),
                cityEditText.getText().toString(),stateEditText.getText().toString(),time);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        Intent intent = new Intent(getApplicationContext(),PaymentMethodActivity.class);
                        intent.putExtra("addressId",dataList.get(position).getId());
                        startActivity(intent);
                        finish();
                    }else {
                        BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
                    }
                }else {
                    BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(AddAddressActivity.this,"Oops something went wrong, Please try again");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.deliverTextView) {
            if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                if (ValidateData()) {
                    if (flag.equalsIgnoreCase("1")) {
                        AddAddress();
                    }else {
                        UpdateAddress();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else {
                BasicFunction.showSnackbar(AddAddressActivity.this, "No internet connection,Please try again..!!");
            }
        }

        /*else if (view.getId() == R.id.homeImgView){

            homeLayout.setBackgroundResource(R.drawable.blueborder);
            officeLayout.setBackgroundResource(R.drawable.blackwhitefillborder);
            satSunLayout.setVisibility(View.GONE);
            homeImageView.setImageResource(R.drawable.selectedradio);
            officeImageView.setImageResource(R.drawable.radio);
            time = "1";

        }else if (view.getId() == R.id.officeImgView){

            officeLayout.setBackgroundResource(R.drawable.blueborder);
            homeLayout.setBackgroundResource(R.drawable.blackwhitefillborder);
            satSunLayout.setVisibility(View.VISIBLE);
            officeImageView.setImageResource(R.drawable.selectedradio);
            homeImageView.setImageResource(R.drawable.radio);
            time = "2";

        }

        else if (view.getId() == R.id.satCheckBox){
            holidayTime();
        }else if (view.getId() == R.id.sunCheckBox){
            holidayTime();
        }*/
    }

    private void holidayTime(){
        if (sunCheckBox.isChecked() && satCheckBox.isChecked()){
            officeTimeTextView.setText("Deliver 10am - 5pm, all days");
            time = "5";
        }
        else if (sunCheckBox.isChecked() && !satCheckBox.isChecked()){
            officeTimeTextView.setText("Deliver 10am - 5pm, all days except Saturday");
            time = "4";
        }
        else if (!sunCheckBox.isChecked() && satCheckBox.isChecked()){
            officeTimeTextView.setText("Deliver 10am - 5pm, Monday to Saturday");
            time = "3";
        }
        else {
            officeTimeTextView.setText("Deliver 10am - 5pm, Monday to Friday");
            time = "2";
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // Intent homeIntent = new Intent(this, MainActivity.class);
                // homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(homeIntent);
                finish();
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }
}
