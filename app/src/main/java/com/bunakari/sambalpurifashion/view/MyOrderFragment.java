package com.bunakari.sambalpurifashion.view;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.OrderAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.OrderList;
import com.bunakari.sambalpurifashion.model.OrderResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment implements OrderAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private TextView notfoundTextView;
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private String uidString;
    private List<OrderResponse> orderResponseList;
    private OrderAdapter orderAdapter;
    private OrderAdapter.ItemClickListener itemClickListener;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_order, container, false);

        initUi(v);

        if (BasicFunction.isNetworkAvailable(getActivity())) {
            GetOrderData();
            dialog.show();
        }else {
            BasicFunction.showSnackbar(getActivity(),"No internet connection,Please try again..!!");
        }

        return v;
    }

    private void initUi(View v) {
        recyclerView = v.findViewById(R.id.orderRecyclerView);
        notfoundTextView = v.findViewById(R.id.notfoundTextView);
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemClickListener = MyOrderFragment.this;

        sharedPreferences = getActivity().getSharedPreferences(GetPrefs.PREFS_NAME,getActivity().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");
    }

    private void GetOrderData(){
        ApiService orderService = RetroClass.getApiService();
        Call<OrderList> orderListCall = orderService.getOrderList(uidString);
        orderListCall.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                dialog.dismiss();
                orderResponseList = new ArrayList<>();
                List<OrderResponse> orderResponses = response.body().getOrderResponseList();
                if (orderResponses != null){
                    orderResponseList.addAll(orderResponses);
                    if (orderResponseList.size() > 0){
                        orderAdapter = new OrderAdapter(getActivity(),itemClickListener,getActivity(),orderResponseList);
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
            public void onFailure(Call<OrderList> call, Throwable t) {
                dialog.dismiss();
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No data found");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag().equals(1)){
            Intent intent = new Intent(getActivity(),AllOrderActivity.class);
            intent.putExtra("oid",orderResponseList.get(position).getOrderid());
            intent.putExtra("ostatus",orderResponseList.get(position).getOrder_status());
            startActivity(intent);
        }
    }
}
