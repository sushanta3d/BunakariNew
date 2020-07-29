package com.bunakari.sambalpurifashion.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.TransactionAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.TransList;
import com.bunakari.sambalpurifashion.model.TransactionResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTransactionActivity extends AppCompatActivity {

    private RecyclerView transRecyclerView;
    private TransactionAdapter transactionAdapter;
    private TextView notfoundTextView;
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private List<TransactionResponse> transList;
    private String uidString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Transaction");

        initUi();

        if (BasicFunction.isNetworkAvailable(getApplicationContext())){
            GetTransaction();
            dialog.show();
        }else {
            BasicFunction.showSnackbar(MyTransactionActivity.this,"No Internet Connection, Please try again..!!");
        }

    }

    private void initUi() {
        transRecyclerView = findViewById(R.id.transRecyclerView);
        notfoundTextView = findViewById(R.id.notfoundTextView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        transRecyclerView.setLayoutManager(layoutManager);

        dialog = new Dialog(MyTransactionActivity.this);
        dialog.setContentView(R.layout.progress_bar);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
    }

    private void GetTransaction(){
        ApiService transService = RetroClass.getApiService();
        Call<TransList> transResponseCall = transService.getTransaction(uidString);
        transResponseCall.enqueue(new Callback<TransList>() {
            @Override
            public void onResponse(Call<TransList> call, Response<TransList> response) {
                dialog.dismiss();
                transList = new ArrayList<>();
                try {
                    if (response.body() != null) {
                        List<TransactionResponse> transactionResponses = response.body().getTransResponseList();
                        transList.addAll(transactionResponses);
                        Log.d("TransResponse",transList.size()+" ");
                        if (transList.size() > 0){
                            transactionAdapter = new TransactionAdapter(getApplicationContext(),transList);
                            transRecyclerView.setAdapter(transactionAdapter);
                        }else {
                            transactionAdapter = new TransactionAdapter(getApplicationContext(),transList);
                            transRecyclerView.setAdapter(transactionAdapter);
                            notfoundTextView.setVisibility(View.VISIBLE);
                            notfoundTextView.setText("Sorry, No Data Found");
                        }
                    }else {
                        notfoundTextView.setVisibility(View.VISIBLE);
                        notfoundTextView.setText("Sorry, No Data Found");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                    notfoundTextView.setVisibility(View.VISIBLE);
                    notfoundTextView.setText("Sorry, No Data Found");
                }
            }

            @Override
            public void onFailure(Call<TransList> call, Throwable t) {
                dialog.dismiss();
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No Data Found");
            }
        });
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
