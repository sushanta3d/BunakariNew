package com.bunakari.sambalpurifashion.view;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.ViewAllAdapter;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CartResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.SubCategoryResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllCategoryActivity extends AppCompatActivity {

    private ArrayList<SubCategoryResponse> responseArrayList = new ArrayList<>();
    private ViewAllAdapter viewAllAdapter;
    private RecyclerView viewAllRecyclerView;
    private TextView textCartItemCount;
    private String catNameString,uidString;
    private SharedPreferences sharedPreferences;
    private int cartcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initUi();

    }

    private void initUi() {
        viewAllRecyclerView = findViewById(R.id.viewAllRecycleView);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        catNameString = getIntent().getStringExtra("catname");
        responseArrayList = (ArrayList) getIntent().getSerializableExtra("datalist");

        getSupportActionBar().setTitle(catNameString);

        setViewData();

    }

    private void setViewData() {
        viewAllRecyclerView.setNestedScrollingEnabled(false);
       // viewAllRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        viewAllRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        viewAllAdapter = new ViewAllAdapter(getApplicationContext(),responseArrayList);
        viewAllRecyclerView.setAdapter(viewAllAdapter);
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

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);

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
    protected void onResume() {
        super.onResume();
        GetCartTotal();
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
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.search:
                Intent searchIntent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(searchIntent);
                return true;
            default:
                return (super.onOptionsItemSelected(item));
        }
    }
}
