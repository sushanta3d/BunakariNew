package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.ProfileResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup modeRadioGroup;
    private RadioButton payTmRadioButton,bankRadioButton,walletRadioButton,cashRadioButton;
    private TextView bankDetailsTextView,subTotalTextView,totalTextView,discountTextView,amt1TextView,amt2TextView,amt3TextView;
    private Button submitButton;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String addIdString,uidString,tAmtString,tItemString,orderIdString,walletAmtString= "0",bankString= "",walletString="";
    private static final long LIMIT = 10000000000L;
    private static long last = 0;
    CheckBox offerCheckBox;
    Float temp,tempTotal;
    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Select Payment Mode");

        initUi();

    }

    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }

    private void initUi() {
        payTmRadioButton = findViewById(R.id.paytmRadioBtn);
        bankRadioButton = findViewById(R.id.bankRadioBtn);
        walletRadioButton = findViewById(R.id.walletRadioBtn);
        cashRadioButton = findViewById(R.id.cashRadioBtn);
        bankDetailsTextView = findViewById(R.id.bankDetailTextView);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);
        modeRadioGroup = findViewById(R.id.radioGroup);
        offerCheckBox = findViewById(R.id.offerCheckBox);
        subTotalTextView = findViewById(R.id.subtotalTextView);
        discountTextView = findViewById(R.id.discountTextView);
        totalTextView = findViewById(R.id.totalTextView);
        amt1TextView = findViewById(R.id.subamtTextView);
        amt2TextView = findViewById(R.id.amt1TextView);
        amt3TextView = findViewById(R.id.amtTextView);
        mainLayout = findViewById(R.id.mainLayout);

        submitButton.setOnClickListener(this);

        addIdString = getIntent().getStringExtra("addressId");

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        tAmtString = sharedPreferences.getString(GetPrefs.PREFS_AMOUNT,"");
        tItemString = sharedPreferences.getString(GetPrefs.PREFS_ITEM,"");

        bankString = getResources().getString(R.string.bankdetailtext);

        tempTotal = Float.parseFloat(tAmtString);

        payTmRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                Float offer = Float.parseFloat(tAmtString) * (0.8f);
                if (Integer.parseInt(String.valueOf(Math.round(offer))) < Integer.parseInt(walletAmtString)){
                    offerCheckBox.setVisibility(View.VISIBLE);
                }else {
                    offerCheckBox.setVisibility(View.GONE);
                }
                if (offerCheckBox.isChecked()){

                }else {
                    totalTextView.setVisibility(View.VISIBLE);
                    amt3TextView.setVisibility(View.VISIBLE);
                    amt3TextView.setText(tAmtString);
                    bankDetailsTextView.setVisibility(View.GONE);
                }
               // cashRadioButton.setVisibility(View.GONE);
            }
        });

        bankRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                if (bankDetailsTextView.getVisibility() == View.VISIBLE) {
                    bankDetailsTextView.setText("");
                    bankDetailsTextView.setText(bankString);
                }else {
                    bankDetailsTextView.setVisibility(View.VISIBLE);
                    bankDetailsTextView.setText("");
                    bankDetailsTextView.setText(bankString);
                }
            }
        });

        walletRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                if (bankDetailsTextView.getVisibility() == View.VISIBLE) {
                    bankDetailsTextView.setText("");
                    bankDetailsTextView.setText("Wallet Amount : " + walletAmtString);
                }else {
                    bankDetailsTextView.setVisibility(View.VISIBLE);
                    bankDetailsTextView.setText("");
                    bankDetailsTextView.setText("Wallet Amount : " + walletAmtString);
                }
            }
        });

        cashRadioButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
             //   payTmRadioButton.setVisibility(View.GONE);
                Float offer = Float.parseFloat(tAmtString) * (0.8f);
                if (Integer.parseInt(String.valueOf(Math.round(offer))) < Integer.parseInt(walletAmtString)){
                    offerCheckBox.setVisibility(View.VISIBLE);
                }else {
                    offerCheckBox.setVisibility(View.GONE);
                }
                if (offerCheckBox.isChecked()){

                }else {
                    totalTextView.setVisibility(View.VISIBLE);
                    amt3TextView.setVisibility(View.VISIBLE);
                    amt3TextView.setText(tAmtString);
                    bankDetailsTextView.setVisibility(View.GONE);
                }
            }
        });

        offerCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                subTotalTextView.setVisibility(View.VISIBLE);
                discountTextView.setVisibility(View.VISIBLE);
                totalTextView.setVisibility(View.VISIBLE);
                amt1TextView.setVisibility(View.VISIBLE);
                amt2TextView.setVisibility(View.VISIBLE);
                amt3TextView.setVisibility(View.VISIBLE);
                discountTextView.setText("Discount : 80% Off");
                amt1TextView.setText(tAmtString);
                temp = Float.parseFloat(tAmtString) * (0.8f);
                amt2TextView.setText(Math.round(temp)+"");
                tempTotal = Float.parseFloat(tAmtString) - temp;
                amt3TextView.setText(Math.round(tempTotal)+"");
            }else{
                subTotalTextView.setVisibility(View.GONE);
                discountTextView.setVisibility(View.GONE);
                amt1TextView.setVisibility(View.GONE);
                amt2TextView.setVisibility(View.GONE);
                tempTotal = Float.parseFloat(tAmtString);
                amt3TextView.setText(Math.round(tempTotal)+"");
            }
        });

    }

    private void GetWalletAmount() {

        ApiService apiService = RetroClass.getApiService();
        Call<ProfileResponse> profileResponseCall = apiService.getWalletAmount(uidString);
        profileResponseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                progressBar.setVisibility(View.GONE);
                ProfileResponse profileResponse = response.body();
                if (profileResponse != null) {
                    walletAmtString = profileResponse.money;
                    mainLayout.setVisibility(View.VISIBLE);
                }else {
                    BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops Something went wrong, Please try again..!!");
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops Something went wrong, Please try again..!!");
            }
        });
    }

    private void SubmitOrder(){

        int radioId = modeRadioGroup.getCheckedRadioButtonId();
        RadioButton modeRadioButton = findViewById(radioId);

        ApiService orderService = RetroClass.getApiService();
        Call<SignupResponse> orderResponseCall = orderService.sendOrder(uidString,tAmtString,tItemString,modeRadioButton.getText().toString(),addIdString);
        orderResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){


                        finish();
                        DelieveryAddressActivity.delieveryAddressActivity.finish();
                        CartActivity.cartActivity.finish();

                        BasicFunction.showToast(getApplicationContext(),"Order Sent Successfully ");
                        Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                        intent.putExtra("from","0");
                        intent.putExtra("amount", Math.round(tempTotal)+"");
                        startActivity(intent);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(GetPrefs.PREFS_ITEM,"");
                        editor.putString(GetPrefs.PREFS_AMOUNT,"");
                        editor.commit();

                    }else {
                        BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
         //   GetWalletAmount();
         //  progressBar.setVisibility(View.VISIBLE);
        }else {
            BasicFunction.showSnackbar(PaymentMethodActivity.this,"No internet connection,Please try again..!!");
        }
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

    @Override
    public void onClick(View view) {
        orderIdString = String.valueOf(getID());
        if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
            if (payTmRadioButton.isChecked()) {
                Intent intent = new Intent(getApplicationContext(),checksum.class);
                intent.putExtra("orderid",orderIdString);
                intent.putExtra("amount", Math.round(tempTotal)+"");
                startActivityForResult(intent,1);
            } else if (bankRadioButton.isChecked()) {
                SubmitOrder();
                progressBar.setVisibility(View.VISIBLE);
            }else if (walletRadioButton.isChecked()) {
                SubmitOrder();
                progressBar.setVisibility(View.VISIBLE);
            }else if (cashRadioButton.isChecked()) {
                SubmitOrder();
                progressBar.setVisibility(View.VISIBLE);
            } else {
                BasicFunction.showToast(getApplicationContext(), "Please select any payment mode");
            }
        } else {
            BasicFunction.showSnackbar(PaymentMethodActivity.this, "No internet connection,Please try again..!!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1) {
            if (resultCode == 2) {
                int position = data.getIntExtra("status",0);
                if (position == 1) {
                    OnlineOrder();
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Cancelled By User");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void OnlineOrder(){
        ApiService orderService = RetroClass.getApiService();
        Call<SignupResponse> orderResponseCall = orderService.sendOrder(uidString, Math.round(tempTotal)+"",tItemString,"PayTm",addIdString);
        orderResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){

                        Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                        intent.putExtra("from","0");
                        intent.putExtra("amount", Math.round(tempTotal)+"");
                        startActivity(intent);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(GetPrefs.PREFS_ITEM,"");
                        editor.putString(GetPrefs.PREFS_AMOUNT,"");
                        editor.commit();

                    }else {
                        BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(PaymentMethodActivity.this,"Oops something went wrong.Please try again!!");
            }
        });
    }
}
