package com.bunakari.sambalpurifashion.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BalanceResponse;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;
import com.google.android.material.textfield.TextInputEditText;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView earnTextView,depositTextView,cashBackTextView;
    private CardView addMoneyCardView,redeemCardView;
   // private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private String uidString,depositString,amountString;
    private Dialog dialog;
    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_balance);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Balance");

        initUi();

    }

    private void initUi() {

        earnTextView = findViewById(R.id.earnValueTextView);
        depositTextView = findViewById(R.id.depositValueTextView);
        cashBackTextView = findViewById(R.id.cbValueTextView);
        addMoneyCardView = findViewById(R.id.addMoneyCardView);
        redeemCardView = findViewById(R.id.redeemCardView);
     //   progressBar = findViewById(R.id.progressBar);
        dialog = new Dialog(MyBalanceActivity.this);
        dialog.setContentView(R.layout.progress_bar);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        depositString = sharedPreferences.getString(GetPrefs.PREFS_DEPOSIT,"");

        addMoneyCardView.setOnClickListener(this);
        redeemCardView.setOnClickListener(this);
    }

    private void GetMybalance(){
        ApiService balanceService = RetroClass.getApiService();
        Call<BalanceResponse> balanceResponseCall = balanceService.getMyBalance(uidString);
        balanceResponseCall.enqueue(new Callback<BalanceResponse>() {
            @Override
            public void onResponse(Call<BalanceResponse> call, Response<BalanceResponse> response) {
                //progressBar.setVisibility(View.GONE);
                dialog.hide();
                BalanceResponse balanceResponse = response.body();
                if (balanceResponse != null) {
                    depositTextView.setText(depositString);
                    cashBackTextView.setText(balanceResponse.cashbackamount);
                    earnTextView.setText(balanceResponse.earnamount);
                }else {
                    BasicFunction.showSnackbar(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<BalanceResponse> call, Throwable t) {
               // progressBar.setVisibility(View.GONE);
                dialog.hide();
                BasicFunction.showSnackbar(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
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

    private void RechargeWallet(){
        ApiService walletService = RetroClass.getApiService();
        Call<SignupResponse> walletResponseCall = walletService.addMoney(uidString,amountString);
        walletResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                dialog.dismiss();
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){

                        Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                        intent.putExtra("from","1");
                        intent.putExtra("amount",amountString);
                        startActivity(intent);

                    }else {
                        BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                dialog.dismiss();
                BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
            }
        });
    }

    private void GetRedeem(){
        ApiService redeemService = RetroClass.getApiService();
        Call<SignupResponse> redeemResponseCall = redeemService.redeemRequest(uidString,amountString);
        redeemResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                dialog.dismiss();
                SignupResponse signupResponse = response.body();
                Log.d("RedeemResponse",signupResponse.success+" "+uidString+" "+amountString);
                if (signupResponse != null){
                    if (signupResponse.success == 1){
                        BasicFunction.showDailogBox(MyBalanceActivity.this,"Redeem request sent successfully. Your amount will be redeem in next 48 hours");
                    }else {
                        BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                dialog.dismiss();
                BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
            }
        });
    }

    private void AddMoney(){
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addMoney(uidString,amountString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                dialog.dismiss();
                SignupResponse signupResponse = response.body();
                if (signupResponse != null){
                    if (signupResponse.success == 1){
                        GetMybalance();
                        dialog.show();
                        BasicFunction.showToast(getApplicationContext(),"Money added successfully into wallet.");
                    }else {
                        BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                    }
                }else {
                    BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                dialog.dismiss();
                BasicFunction.showDailogBox(MyBalanceActivity.this,"Oops something went wrong.Please try again!!");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addMoneyCardView){
           addMoneyInWallet();
        }else if (view.getId() == R.id.redeemCardView){
            if (Integer.parseInt(earnTextView.getText().toString()) >= 1000){
                redeemRequest();
            }else {
                BasicFunction.showDailogBox(MyBalanceActivity.this,"You do not have sufficient amount for redeem. Please grow your network and earn more amount.");
            }
        }
    }

    private void addMoneyInWallet(){
        final Dialog moneydialog = new Dialog(MyBalanceActivity.this);
        moneydialog.setContentView(R.layout.addmoney_layout);

        final TextInputEditText amountEditText = moneydialog.findViewById(R.id.amountEditText);
        Button proceedButton = moneydialog.findViewById(R.id.submitButton);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountEditText.getText().toString().equalsIgnoreCase("")){
                    amountEditText.setError("Required Field..");
                }else {
                    amountString = amountEditText.getText().toString();
                    AddMoney();
                    dialog.show();
                    moneydialog.hide();
                }
            }
        });
        moneydialog.show();
    }

    private void redeemRequest(){
        final Dialog moneydialog = new Dialog(MyBalanceActivity.this);
        moneydialog.setContentView(R.layout.addmoney_layout);

        final TextInputEditText amountEditText = moneydialog.findViewById(R.id.amountEditText);
        TextView titleTextView = moneydialog.findViewById(R.id.titleTextView);
        Button proceedButton = moneydialog.findViewById(R.id.submitButton);
        titleTextView.setText("Redeem Amount");
        proceedButton.setText("Proceed to Redeem");

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amountEditText.getText().toString().equalsIgnoreCase("")){
                    amountEditText.setError("Required Field..");
                }else {
                    amountString = amountEditText.getText().toString();
                    if (Integer.parseInt(amountString) <= Integer.parseInt(earnTextView.getText().toString())) {
                        GetRedeem();
                        dialog.show();
                        moneydialog.hide();
                    }else {
                        BasicFunction.showDailogBox(MyBalanceActivity.this,"You do not have sufficient amount for redeem. Please grow your network and earn more amount.");
                    }
                }
            }
        });
        moneydialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getApplicationContext())){
            GetMybalance();
            dialog.show();
        }else {
            BasicFunction.showSnackbar(MyBalanceActivity.this,"No Internet Connection, Please try again..!!");
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
