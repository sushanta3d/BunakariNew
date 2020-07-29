package com.bunakari.sambalpurifashion.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.LandsNetworkAdapter;
import com.bunakari.sambalpurifashion.adapter.NetworkAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.NetworkList;
import com.bunakari.sambalpurifashion.model.NetworkResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNetworkActivity extends AppCompatActivity implements NetworkAdapter.ItemClickListener {

    private SharedPreferences sharedPreferences;
    private String nameString,refIdString;
    private List<NetworkResponse> networkResponseList;
    private ArrayList<String> refCodeArrayList;
    private Dialog dialog;
    private RecyclerView networkRecyclerView,landsNwRecyclerView;
    private RelativeLayout topLayout;
    private NetworkAdapter networkAdapter;
    private LandsNetworkAdapter landsNetworkAdapter;
    private NetworkAdapter.ItemClickListener itemClickListener;
    private TextView nameTextView,memberTextView,refCodeTextView,notfoundTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_network);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Network");

        initUi();

        if (BasicFunction.isNetworkAvailable(getApplicationContext())){
            GetNetworkData(0);
            dialog.show();
        }else {
            BasicFunction.showSnackbar(MyNetworkActivity.this,"No Internet Connection, Please try again..!!");
        }
    }

    private void initUi() {
        networkRecyclerView = findViewById(R.id.networkRecyclerView);
        landsNwRecyclerView = findViewById(R.id.tempNwRecyclerView);
        nameTextView = findViewById(R.id.nameTextView);
        memberTextView = findViewById(R.id.membersTextView);
        refCodeTextView = findViewById(R.id.refCodeTextView);
        topLayout = findViewById(R.id.topLayout);

        notfoundTextView = findViewById(R.id.notfoundTextView);
        dialog = new Dialog(MyNetworkActivity.this);
        dialog.setContentView(R.layout.progress_bar);

        landsNwRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        networkRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        refCodeArrayList = new ArrayList<>();
        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        nameString = sharedPreferences.getString(GetPrefs.PREFS_UNAME,"");
        refIdString = sharedPreferences.getString(GetPrefs.PREFS_APP_RFERRAL_CODE,"");

        itemClickListener = MyNetworkActivity.this;

        nameTextView.setText(nameString);
        refCodeTextView.setText(refIdString);
        memberTextView.setText("Total Members : 0");
    }

    private void GetNetworkData(int position){
            ApiService transService = RetroClass.getApiService();
            Call<NetworkList> transResponseCall = transService.getNetwork(refIdString);
            transResponseCall.enqueue(new Callback<NetworkList>() {
                @Override
                public void onResponse(Call<NetworkList> call, Response<NetworkList> response) {
                    dialog.dismiss();
                    networkResponseList = new ArrayList<>();
                    try {
                        if (response.body() != null) {
                            List<NetworkResponse> networkResponses = response.body().getResponseList();
                            networkResponseList.addAll(networkResponses);
                            Log.d("NwResponse",networkResponseList.size()+" ");
                            if (networkResponseList.size() > 0){
                                refCodeArrayList = new ArrayList<>();
                                memberTextView.setText("Total Members : "+networkResponseList.size());
                                refCodeArrayList.add(refIdString);

                                landsNetworkAdapter = new LandsNetworkAdapter(getApplicationContext(),refCodeArrayList,position);
                                landsNwRecyclerView.setAdapter(landsNetworkAdapter);

                                networkAdapter = new NetworkAdapter(itemClickListener,getApplicationContext(),networkResponseList);
                                networkRecyclerView.setAdapter(networkAdapter);
                            }else {
                                topLayout.setVisibility(View.GONE);
                                networkAdapter = new NetworkAdapter(itemClickListener,getApplicationContext(),networkResponseList);
                                networkRecyclerView.setAdapter(networkAdapter);
                                notfoundTextView.setVisibility(View.VISIBLE);
                                notfoundTextView.setText("Sorry, No Data Found");
                            }
                        }else {
                            topLayout.setVisibility(View.GONE);
                            notfoundTextView.setVisibility(View.VISIBLE);
                            notfoundTextView.setText("Sorry, No Data Found");
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        topLayout.setVisibility(View.GONE);
                        notfoundTextView.setVisibility(View.VISIBLE);
                        notfoundTextView.setText("Sorry, No Data Found");
                    }
                }

                @Override
                public void onFailure(Call<NetworkList> call, Throwable t) {
                    dialog.dismiss();
                    notfoundTextView.setVisibility(View.VISIBLE);
                    notfoundTextView.setText("Sorry, No Data Found");
                }
            });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (!networkResponseList.get(position).getUserappreferralcode().equals(refIdString)) {
            if (networkResponseList.get(position).getUsernetworkcount().equalsIgnoreCase("0")) {
                BasicFunction.showToast(getApplicationContext(), "No members found in this network");
            } else {
                if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                    refIdString = networkResponseList.get(position).userappreferralcode;
                    GetNetworkData(position);
                    dialog.show();
                } else {
                    BasicFunction.showSnackbar(MyNetworkActivity.this, "No Internet Connection, Please try again..!!");
                }
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
