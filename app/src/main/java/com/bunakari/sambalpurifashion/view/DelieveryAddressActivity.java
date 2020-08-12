package com.bunakari.sambalpurifashion.view;

import android.app.AlertDialog;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.AddressAdapter;
import com.bunakari.sambalpurifashion.model.AddressList;
import com.bunakari.sambalpurifashion.model.AddressResponse;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelieveryAddressActivity extends AppCompatActivity implements AddressAdapter.ItemClickListener, View.OnClickListener {

    private RecyclerView addressRecyclerView;
    private AddressAdapter addressAdapter;
    private AddressAdapter.ItemClickListener itemClickListener;
    private TextView notfoundTextView,addAddressTextView;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;
    private String uidString;
    private ArrayList<AddressResponse> addressResponseList;
    private LinearLayoutManager layoutManager;
    public static DelieveryAddressActivity delieveryAddressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delievery_address);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Select Delivery Address");

        initUi();

    }

    private void initUi() {
        addressRecyclerView = findViewById(R.id.addressRecycleView);
        notfoundTextView = findViewById(R.id.notfoundTextView);
        addAddressTextView = findViewById(R.id.addAddressTextView);
        progressBar = findViewById(R.id.progressBar);

        addressRecyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addressRecyclerView.setLayoutManager(layoutManager);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GetPrefs.PREFS_ADD_POS,"0");
        editor.commit();

        itemClickListener = DelieveryAddressActivity.this;
        delieveryAddressActivity = DelieveryAddressActivity.this;

        addAddressTextView.setOnClickListener(this);
    }

    private void GetAddressData(){
        ApiService addressService = RetroClass.getApiService();
        Call<AddressList> addressListCall = addressService.getAddress(uidString);
        addressListCall.enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                progressBar.setVisibility(View.GONE);
                addressResponseList = new ArrayList<>();
                try {
                    List<AddressResponse> addressResponses = response.body().getAddressResponseList();
                    addressResponseList.addAll(addressResponses);
                    if (addressResponses != null) {
                        if (addressResponseList.size() > 0) {
                            notfoundTextView.setVisibility(View.GONE);
                            addressAdapter = new AddressAdapter(itemClickListener, getApplicationContext(), addressResponseList);
                            addressRecyclerView.setAdapter(addressAdapter);
                        } else {
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
            public void onFailure(Call<AddressList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No Data Found");
            }
        });
    }

    private void DeleteAddress( int position){
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.deleteAddress(addressResponseList.get(position).getId());
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        GetAddressData();
                    }else {
                        BasicFunction.showDailogBox(DelieveryAddressActivity.this,"Oops something went wrong, Please try again");
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showDailogBox(DelieveryAddressActivity.this,"Oops something went wrong, Please try again");
            }
        });
    }

    @Override
    public void onItemClick(View view, final int position) {
        if (view.getTag().equals(1)){
            Log.d("pos",position+" ");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(GetPrefs.PREFS_ADD_POS,position+"");
            editor.commit();
            addressAdapter.notifyDataSetChanged();
        }else if (view.getTag().equals(2)){
            Intent intent = new Intent(getApplicationContext(),PaymentMethodActivity.class);
            intent.putExtra("addressId",addressResponseList.get(position).getId());
            startActivity(intent);
        }else if (view.getTag().equals(3)){
            Intent intent = new Intent(getApplicationContext(),AddAddressActivity.class);
            intent.putExtra("flag","2");
            intent.putExtra("datalist",addressResponseList);
            intent.putExtra("position",position);
            startActivity(intent);
        }else if (view.getTag().equals(4)){
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(DelieveryAddressActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("Are you sure you want to delete this address ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        DeleteAddress(position);
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        BasicFunction.showSnackbar(DelieveryAddressActivity.this,"No internet connection,Please try again..!!");
                    }
                }
            });
            builder.setNegativeButton("No",null);
            builder.show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(),AddAddressActivity.class);
        intent.putExtra("flag","1");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
            GetAddressData();
        }else {
            BasicFunction.showSnackbar(DelieveryAddressActivity.this,"No internet connection,Please try again..!!");
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
}
