package com.bunakari.sambalpurifashion.view;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.AllOrderAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.OrderDetailsList;
import com.bunakari.sambalpurifashion.model.OrderDetailsResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllOrderActivity extends AppCompatActivity implements AllOrderAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private TextView notfoundTextView;
    private Dialog dialog;
    private List<OrderDetailsResponse> orderResponseList;
    private String oidString,ostatusString,uid;
    private AllOrderAdapter orderAdapter;
    private AllOrderAdapter.ItemClickListener itemClickListener;
    SharedPreferences sharedPreferences;
    public static AllOrderActivity orderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("All Order");

        initUi();


    }

    private void initUi() {
        recyclerView = findViewById(R.id.orderDetailRecyclerView);
        notfoundTextView = findViewById(R.id.notfoundTextView);
        dialog = new Dialog(AllOrderActivity.this);
        dialog.setContentView(R.layout.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        itemClickListener = AllOrderActivity.this;

        oidString = getIntent().getStringExtra("oid");
        ostatusString = getIntent().getStringExtra("ostatus");

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uid = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        orderActivity = AllOrderActivity.this;
    }

    private void GetAllOrderData(){
        ApiService orderService = RetroClass.getApiService();
        Call<OrderDetailsList> detailsListCall = orderService.getAllOrderList(oidString);
        detailsListCall.enqueue(new Callback<OrderDetailsList>() {
            @Override
            public void onResponse(Call<OrderDetailsList> call, Response<OrderDetailsList> response) {
                dialog.dismiss();
                orderResponseList = new ArrayList<>();
                assert response.body() != null;
                List<OrderDetailsResponse> orderResponses = response.body().getOrderDetailsResponseList();
                if (orderResponses != null){

                    orderResponseList.addAll(orderResponses);
                    if (orderResponseList.size() > 0){
                        orderAdapter = new AllOrderAdapter(itemClickListener,getApplicationContext(),orderResponseList,ostatusString,oidString);
                        recyclerView.setAdapter(orderAdapter);
                    }else {
                        notfoundTextView.setVisibility(View.VISIBLE);
                        notfoundTextView.setText("Sorry, No data found");
                    }
                }else {
                    notfoundTextView.setVisibility(View.VISIBLE);
                    notfoundTextView.setText("Sorry, No data found");
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsList> call, Throwable t) {
                dialog.dismiss();
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No data found");
            }
        });
    }


    @Override
    public void onItemClick(View view, int position, Float rate) {
      //  UpdateRating(orderResponseList.get(position).getPid(),String.valueOf(rate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
            GetAllOrderData();
            dialog.show();
        }else {
            BasicFunction.showSnackbar(AllOrderActivity.this,"No internet connection,Please try again..!!");
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
