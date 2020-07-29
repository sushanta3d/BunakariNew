package com.bunakari.sambalpurifashion.view;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.WishlistAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.ProductResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.model.WishlistList;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment implements WishlistAdapter.ItemClickListener {

    TextView notfoundTextView;
    RecyclerView productRecyclerView;
    ProgressBar progressBar;
    String mobileString,uidString;
    ArrayList<ProductResponse> productResponseList;
    WishlistAdapter wishlistAdapter;
    private WishlistAdapter.ItemClickListener itemClickListener;
    private SharedPreferences sharedPreferences;
    private GridLayoutManager gridLayoutManager;

    public WishlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_wishlist, container, false);

        initUi(v);

        return v;
    }

    private void initUi(View v) {

        notfoundTextView = v.findViewById(R.id.notfoundTextView);
        productRecyclerView = v.findViewById(R.id.productRecyclerView);
        progressBar = v.findViewById(R.id.progressBar);

        productRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        itemClickListener = WishlistFragment.this;

        sharedPreferences = getActivity().getSharedPreferences(GetPrefs.PREFS_NAME,getActivity().MODE_PRIVATE);
        mobileString = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");
    }

    private void GetProductData() {
        ApiService productService = RetroClass.getApiService();
        Call<WishlistList> prodServiceList = productService.getWishlist(mobileString);

        prodServiceList.enqueue(new Callback<WishlistList>() {
            @Override
            public void onResponse(Call<WishlistList> call, Response<WishlistList> response) {
                progressBar.setVisibility(View.GONE);
                productResponseList = new ArrayList<>();
                try {
                    List<ProductResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getWishResponseList();
                        productResponseList.addAll(pageResponses);

                        if (productResponseList.size() > 0) {
                            wishlistAdapter = new WishlistAdapter(itemClickListener,getActivity(), productResponseList);
                            productRecyclerView.setAdapter(wishlistAdapter);
                            productRecyclerView.setLayoutManager(gridLayoutManager);
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
            public void onFailure(Call<WishlistList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No Data Found");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (BasicFunction.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            GetProductData();
        }else {
            BasicFunction.showSnackbar(getActivity(),"No internet connection,Please try again..!!");
        }
    }

    private void RemoveWishlist(final int pos){
        progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.removeWishlist(productResponseList.get(pos).getId(),mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        GetProductData();
                        progressBar.setVisibility(View.VISIBLE);
                        BasicFunction.showToast(getActivity(),"Item remove from wishlist");
                    }else {
                        BasicFunction.showToast(getActivity(),"Item can't remove from wishlist1");
                    }
                }else {
                    BasicFunction.showToast(getActivity(),"Item can't remove from wishlist1");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showToast(getActivity(),"Item can't remove from wishlist");
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag().equals(1)) {
            Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
            intent.putExtra("datalist", productResponseList);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            RemoveWishlist(position);
        }
    }

}
