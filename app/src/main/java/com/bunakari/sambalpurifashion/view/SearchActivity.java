package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.SearchAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CartResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.ProductList;
import com.bunakari.sambalpurifashion.model.ProductResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.ItemClickListener {

    TextView notfoundTextView,textCartItemCount;
    EditText searchEditText;
    RecyclerView productRecyclerView;
    ProgressBar progressBar;
    String mobileString,uidString;
    ArrayList<ProductResponse> productResponseList;
    ArrayList<Integer> wishflag = new ArrayList<>();
    SearchAdapter searchAdapter;
    private SearchAdapter.ItemClickListener itemClickListener;
    private SharedPreferences sharedPreferences;
    private int cartcount = 0;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Search design through id ");

        initUi();

    }

    private void initUi() {

        searchEditText = findViewById(R.id.searchEditText);
        notfoundTextView = findViewById(R.id.notfoundTextView);
        productRecyclerView = findViewById(R.id.productRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        productRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

        itemClickListener = SearchActivity.this;

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        mobileString = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        searchEditText.setOnEditorActionListener((textView, actionid, keyEvent) -> {
            if(actionid == EditorInfo.IME_ACTION_SEARCH){
                if(searchEditText.getText().toString().equalsIgnoreCase("")){
                    BasicFunction.showSnackbar(SearchActivity.this,"Field Required..!!");
                    InputMethodManager inputManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        GetProductData();
                        progressBar.setVisibility(View.VISIBLE);
                    }else {
                        BasicFunction.showSnackbar(SearchActivity.this,"No internet connection,Please try again..!!");
                    }

                    InputMethodManager inputManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            return false;
        });
    }

    private void GetProductData() {
        ApiService productService = RetroClass.getApiService();
        Call<ProductList> prodServiceList = productService.getSearch(mobileString,searchEditText.getText().toString());

        prodServiceList.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                progressBar.setVisibility(View.GONE);
                productResponseList = new ArrayList<>();
                try {
                    List<ProductResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getProductResponseList();
                        productResponseList.addAll(pageResponses);

                        if (productResponseList.size() > 0) {
                            wishflag = new ArrayList<>();
                            for (int i = 0; i < productResponseList.size(); i++) {
                                wishflag.add(0);
                            }
                            for (int i = 0; i < productResponseList.size(); i++) {
                                wishflag.set(i,productResponseList.get(i).getInwishlist());
                            }
                            notfoundTextView.setVisibility(View.GONE);
                            searchAdapter = new SearchAdapter(itemClickListener,getApplicationContext(), productResponseList,wishflag);
                            productRecyclerView.setAdapter(searchAdapter);
                            productRecyclerView.setLayoutManager(gridLayoutManager);
                        } else {
                            searchAdapter = new SearchAdapter(itemClickListener,getApplicationContext(), productResponseList,wishflag);
                            productRecyclerView.setAdapter(searchAdapter);
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
            public void onFailure(Call<ProductList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                notfoundTextView.setVisibility(View.VISIBLE);
                notfoundTextView.setText("Sorry, No Data Found");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(searchEditText.getText().length() != 0) {
            if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                GetProductData();
                progressBar.setVisibility(View.VISIBLE);
            }else {
                BasicFunction.showSnackbar(SearchActivity.this,"No internet connection,Please try again..!!");
            }
        }
        GetCartTotal();
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

    private void GetCartTotal(){
        ApiService cartService = RetroClass.getApiService();
        Call<CartList> cartListCall = cartService.getCart(uidString);
        cartListCall.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                List<CartResponse> cartResponseList = null;
                if (response.body() != null) {
                    cartResponseList = response.body().getCartResponseList();
                    cartcount = cartResponseList.size();
                    if(cartResponseList.size() > 0){
                        if (textCartItemCount != null) {
                            textCartItemCount.setText(cartcount+"");
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }else {
                        if (textCartItemCount != null) {
                            if (cartcount == 0) {
                                if (textCartItemCount.getVisibility() != View.GONE) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                //
            }
        });
    }

    private void AddWishlist(final int pos){
        progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addWishlist(productResponseList.get(pos).getId(),mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        productResponseList.get(pos).setInwishlist(1);
                        BasicFunction.showToast(getApplicationContext(),"Item added to wishlist");
                    }else {
                        wishflag.set(pos,0);
                        searchAdapter.updateWishlist(wishflag);
                        BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist");
                    }
                }else {
                    wishflag.set(pos,0);
                    searchAdapter.updateWishlist(wishflag);
                    BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                wishflag.set(pos,0);
                searchAdapter.updateWishlist(wishflag);
                BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist1");
            }
        });
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
                        productResponseList.get(pos).setInwishlist(0);
                        BasicFunction.showToast(getApplicationContext(),"Item remove from wishlist");
                    }else {
                        wishflag.set(pos,1);
                        searchAdapter.updateWishlist(wishflag);
                        BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist1");
                    }
                }else {
                    wishflag.set(pos,1);
                    searchAdapter.updateWishlist(wishflag);
                    BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist1");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                wishflag.set(pos,1);
                searchAdapter.updateWishlist(wishflag);
                BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        final MenuItem menuItem1 = menu.findItem(R.id.search);

        menuItem1.setVisible(false);
        menuItem.setActionView(R.layout.actionbarlayout);

        View actionView = MenuItemCompat.getActionView(menuItem);

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
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
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            default:
                return (super.onOptionsItemSelected(item));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag().equals(1)) {
            Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
            intent.putExtra("datalist", productResponseList);
            intent.putExtra("position", position);
            startActivity(intent);
        }else {
            if (wishflag.get(position) == 0){
                AddWishlist(position);
                wishflag.set(position,1);
                searchAdapter.updateWishlist(wishflag);
            }else {
                RemoveWishlist(position);
                wishflag.set(position,0);
                searchAdapter.updateWishlist(wishflag);
            }
        }
    }
}
