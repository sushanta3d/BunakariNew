package com.bunakari.sambalpurifashion.view;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.ColorAdapter;
import com.bunakari.sambalpurifashion.adapter.CustomSwipeAdapter;
import com.bunakari.sambalpurifashion.adapter.SizeAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CartResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.HeightWrapperViewPager;
import com.bunakari.sambalpurifashion.model.ProductResponse;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.network.ApiService;
import com.bunakari.sambalpurifashion.network.RetroClass;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener , SizeAdapter.ItemClickListener, ColorAdapter.ItemClickListener {

    private ArrayList<ProductResponse> productResponseList = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();
    private RecyclerView colorRecyclerView,sizeRecyclerView;
    private SizeAdapter sizeAdapter;
    private ColorAdapter colorAdapter;
    private SizeAdapter.ItemClickListener itemClickListener1;
    private ColorAdapter.ItemClickListener itemClickListener2;
    private HeightWrapperViewPager viewPager;
    private ProgressBar progressBar;
    private ImageView prevImgview,nextImgview;
    private ImageView shareImgView,wishImgView,plusImgView,minusImgView;
    private WebView descriptionWebView,disclaimerWebView;
    private TextView pNameTextView,priceTextView,offerPriceTextView,percentTextView,qtyTextView,addCartTextView,textCartItemCount,descTitleTextView,disclaimTitleTextView;
    private int pos = 0, total;
    private int wishflag = 0;
    private ArrayList<Integer> sizePosList = new ArrayList<>();
    private ArrayList<Integer> colorPosList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String mobileString,uidString,sizeId = "",colorId = "";
    private int cartcount = 0;
    private CardView sizeCardView,colorCardView;

    private RatingBar rateBar;
    boolean videoflag = false;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Product Details");

        initUi();

        setViewData();

    }

    private void initUi() {

        if(!BasicFunction.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        colorRecyclerView = findViewById(R.id.colorRecycleView);
        sizeRecyclerView = findViewById(R.id.sizeRecycleView);
        sizeCardView = findViewById(R.id.sizeCardView);
        colorCardView = findViewById(R.id.colorCardView);

        colorRecyclerView.setNestedScrollingEnabled(false);
        colorRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));

        sizeRecyclerView.setNestedScrollingEnabled(false);
        sizeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));

        viewPager = findViewById(R.id.viewPager);

        prevImgview = findViewById(R.id.leftImgbtn);
        nextImgview = findViewById(R.id.rightImgbtn);

        shareImgView = findViewById(R.id.shareImgView);
        wishImgView = findViewById(R.id.wishImgView);
        plusImgView = findViewById(R.id.plusImgView);
        minusImgView = findViewById(R.id.minusImgView);

        descriptionWebView = findViewById(R.id.descWebView);
        disclaimerWebView = findViewById(R.id.disclaimWebView);
        descTitleTextView = findViewById(R.id.descTitleTextView);
        disclaimTitleTextView = findViewById(R.id.disclaimTitleTextView);

        pNameTextView = findViewById(R.id.pNameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        offerPriceTextView = findViewById(R.id.offerPriceTextView);
        percentTextView = findViewById(R.id.percentTextView);
        qtyTextView = findViewById(R.id.qtyTextView);
        addCartTextView = findViewById(R.id.addCartTextView);
        progressBar = findViewById(R.id.progressBar);

        productResponseList = (ArrayList) getIntent().getSerializableExtra("datalist");
        pos = getIntent().getIntExtra("position",0);

        wishImgView.setOnClickListener(this);
        minusImgView.setOnClickListener(this);
        plusImgView.setOnClickListener(this);
        addCartTextView.setOnClickListener(this);

        itemClickListener1 = ProductDetailsActivity.this;
        itemClickListener2 = ProductDetailsActivity.this;
      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        mobileString = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

    private void setViewData(){

        wishflag = productResponseList.get(pos).getInwishlist();
        if (wishflag == 1){
            wishImgView.setImageResource(R.drawable.ic_wishlistfill);
        }else {
            wishImgView.setImageResource(R.drawable.ic_wishlist);
        }

        for (int i = 0; i < productResponseList.get(pos).getSize().size(); i++) {
            sizePosList.add(i,0);
        }

        for (int i = 0; i < productResponseList.get(pos).getColor().size(); i++) {
            colorPosList.add(i,0);
        }

        pNameTextView.setText(productResponseList.get(pos).getProname());


        if (!productResponseList.get(pos).getOffer_price().equalsIgnoreCase("0")) {
            offerPriceTextView.setText("\u20B9 "+productResponseList.get(pos).getPrice());
            offerPriceTextView.setPaintFlags(offerPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            priceTextView.setText("\u20B9 "+productResponseList.get(pos).getOffer_price());
        }else {
            priceTextView.setText("\u20B9 "+productResponseList.get(pos).getPrice());
            offerPriceTextView.setVisibility(View.GONE);
            percentTextView.setVisibility(View.GONE);
        }

        if (productResponseList.get(pos).getDescription().length() != 0){
            descriptionWebView.loadData(productResponseList.get(pos).getDescription(),"text/html",null);
        }else {
            descTitleTextView.setVisibility(View.GONE);
            descriptionWebView.setVisibility(View.GONE);
        }

        if (productResponseList.get(pos).getDesclaimer().length() != 0){
            disclaimerWebView.loadData(productResponseList.get(pos).getDesclaimer(),"text/html",null);
        }else {
            disclaimTitleTextView.setVisibility(View.GONE);
            disclaimerWebView.setVisibility(View.GONE);
        }

        if(productResponseList.get(pos).getImg().length() != 60){
            imgList.add(productResponseList.get(pos).getImg().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg1().length() != 60){
            imgList.add(productResponseList.get(pos).getImg1().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg2().length() != 60){
            imgList.add(productResponseList.get(pos).getImg2().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg3().length() != 60){
            imgList.add(productResponseList.get(pos).getImg3().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg4().length() != 60){
            imgList.add(productResponseList.get(pos).getImg4().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg5().length() != 60){
            imgList.add(productResponseList.get(pos).getImg5().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg6().length() != 60){
            imgList.add(productResponseList.get(pos).getImg6().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg7().length() != 60){
            imgList.add(productResponseList.get(pos).getImg7().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg8().length() != 60){
            imgList.add(productResponseList.get(pos).getImg8().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg9().length() != 64){
            videoflag = true;
            imgList.add(productResponseList.get(pos).getImg9().replaceAll(" ","%20"));
        }

        viewPager.setAdapter(new CustomSwipeAdapter(getApplicationContext(),imgList){
            public Object instantiateItem(ViewGroup container, final int position) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View swipe_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
                ImageView imageView = (ImageView)swipe_view.findViewById(R.id.image_view);
                final ProgressBar progressBar = (ProgressBar)swipe_view.findViewById(R.id.progressBar);



                    BasicFunction.showImage(imgList.get(position),getApplicationContext(),imageView,progressBar);



                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            Intent localIntent = new Intent(getApplicationContext(),
                                    FullscreenActivity.class);
                            localIntent.putExtra("pos", position);
                            localIntent.putExtra("image", imgList);
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(localIntent);
                        }

                });
                imageView.setTag(position);

                shareImgView.setOnClickListener(view -> {
                    ImageView proImgView = (ImageView)viewPager.findViewWithTag(viewPager.getCurrentItem());
                    proImgView.setDrawingCacheEnabled(true);

                    Uri bmpUri = getLocalBitmapUri(proImgView);
                    if (bmpUri != null) {
                        Spanned spanned = Html.fromHtml(Html.fromHtml(productResponseList.get(pos).getDescription()).toString());
                        String boldHeader = "*Product Enquiry*";
                        String appRefCode = sharedPreferences.getString(GetPrefs.PREFS_APP_RFERRAL_CODE,"");
                        String shareBody = "Join me on Free India Market, Enter my code " + appRefCode + " to earn ₹100 back.\nhttps://play.google.com/store/apps/details?id="
                                + getApplicationContext().getPackageName();
                        // Construct a ShareIntent with link to image
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, boldHeader+"\n\n"+"*Product Name:* " + productResponseList.get(pos).getProname() + "\n\n" + "*Description:* \n\n" + spanned+"\n\n"+"https://freeindiamarket.com/Item_detail/"+ productResponseList.get(pos).getId() +"/"+productResponseList.get(pos).getProname().replaceAll(" ","+")+"\n\n"+shareBody);
                        shareIntent.setType("image*//*");
                        // Launch sharing dialog for image
                        startActivity(Intent.createChooser(shareIntent, "Share Product " + productResponseList.get(pos).getProname()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Not able to share this product...", Toast.LENGTH_LONG).show();
                    }
                });

                container.addView(swipe_view);
                return swipe_view;
            }
        });

        prevImgview.setVisibility(View.GONE);

        final int last = imgList.size();

        if (last == 1) {
            prevImgview.setVisibility(View.GONE);
            nextImgview.setVisibility(View.GONE);
        } else {
            prevImgview.setVisibility(View.GONE);
            nextImgview.setVisibility(View.VISIBLE);
        }

        nextImgview.setOnClickListener(v -> {
            int curr = viewPager.getCurrentItem();

            Log.d("last count", "" + last);
            Log.d("next button", "" + curr);
            prevImgview.setVisibility(View.VISIBLE);


            nextImgview.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(curr + 1, true);
            Log.d("next button", "" + curr);
            if ((curr + 1) == (last - 1)) {
                nextImgview.setVisibility(View.GONE);
            }


        });

        prevImgview.setOnClickListener(v -> {
            nextImgview.setVisibility(View.VISIBLE);
            int curr = viewPager.getCurrentItem();
            prevImgview.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(curr - 1);
            if ((curr - 1) == 0) {
                prevImgview.setVisibility(View.GONE);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    nextImgview.setVisibility(View.VISIBLE);
                    prevImgview.setVisibility(View.GONE);
                } else if (position == (last - 1)) {
                    nextImgview.setVisibility(View.GONE);
                    prevImgview.setVisibility(View.VISIBLE);
                } else {
                    nextImgview.setVisibility(View.VISIBLE);
                    prevImgview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        if (productResponseList.get(pos).getSize().size() > 0) {
            sizeAdapter = new SizeAdapter(itemClickListener1, getApplicationContext(), productResponseList.get(pos).getSize(), sizePosList);
            sizeRecyclerView.setAdapter(sizeAdapter);
        }else {
            sizeCardView.setVisibility(View.GONE);
        }

        if (productResponseList.get(pos).getColor().size() > 0) {
            colorAdapter = new ColorAdapter(itemClickListener2, getApplicationContext(), productResponseList.get(pos).getColor(), colorPosList);
            colorRecyclerView.setAdapter(colorAdapter);
        }else {
            colorCardView.setVisibility(View.GONE);
        }

    }

    private void AddWishlist(){
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
                        wishflag = 1;
                        BasicFunction.showToast(getApplicationContext(),"Item added to wishlist");
                    }else {
                        wishImgView.setImageResource(R.drawable.ic_wishlist);
                        BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist");
                    }
                }else {
                    wishImgView.setImageResource(R.drawable.ic_wishlist);
                    BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                wishImgView.setImageResource(R.drawable.ic_wishlist);
                BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist1");
            }
        });
    }

    private void RemoveWishlist(){
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
                        wishflag = 0;
                        BasicFunction.showToast(getApplicationContext(),"Item remove from wishlist");
                    }else {
                        wishImgView.setImageResource(R.drawable.ic_wishlistfill);
                        BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist1");
                    }
                }else {
                    wishImgView.setImageResource(R.drawable.ic_wishlistfill);
                    BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist1");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                wishImgView.setImageResource(R.drawable.ic_wishlistfill);
                BasicFunction.showToast(getApplicationContext(),"Item can't remove from wishlist");
            }
        });
    }

    private void AddToCart(){
        progressBar.setVisibility(View.VISIBLE);
        getTotalAmount();
        ApiService addCartService = RetroClass.getApiService();
        Call<SignupResponse> cartResponseCall = addCartService.addToCart(uidString,
                productResponseList.get(pos).getId(),colorId,sizeId,qtyTextView.getText().toString(),total+"");
        cartResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        BasicFunction.showToast(getApplicationContext(),"Item added to cart");
                        GetCartTotal();
                    }else {
                        BasicFunction.showToast(getApplicationContext(),"Item can't added to cart");
                    }
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Item can't added to cart");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                BasicFunction.showToast(getApplicationContext(),"Item can't added to cart");
            }
        });
    }

    private void getTotalAmount(){
        total = 0;
        if (productResponseList.get(pos).getOffer_price().length() != 0) {
            total = total + (Integer.parseInt(productResponseList.get(pos).getOffer_price()) * Integer.parseInt(qtyTextView.getText().toString()));
        }else {
            total = total + (Integer.parseInt(productResponseList.get(pos).getPrice()) * Integer.parseInt(qtyTextView.getText().toString()));
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            //Log.d("bmp",bmp+ " " );
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), getString(R.string.app_name) + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void getSizeColor(){
        Log.d("SizeLength",productResponseList.get(pos).getSize().size()+" ");
        if (productResponseList.get(pos).getSize().size() > 0) {
            if (sizePosList.contains(1)) {
                for (int i = 0; i < sizePosList.size(); i++) {
                    if (sizePosList.get(i) == 1) {
                        sizeId = productResponseList.get(pos).getSize().get(i).getSize();
                    }
                }

                if (colorPosList.contains(1)) {
                    for (int i = 0; i < colorPosList.size(); i++) {
                        if (colorPosList.get(i) == 1) {
                            colorId = productResponseList.get(pos).getColor().get(i).getId();
                        }
                    }
                } else {
                    BasicFunction.showToast(getApplicationContext(), "Please select a color");
                }
            } else {
                BasicFunction.showToast(getApplicationContext(), "Please select a size");
            }
        }else {
            if (colorPosList.contains(1)) {
                for (int i = 0; i < colorPosList.size(); i++) {
                    if (colorPosList.get(i) == 1) {
                        colorId = productResponseList.get(pos).getColor().get(i).getId();
                    }
                }
            } else {
                BasicFunction.showToast(getApplicationContext(), "Please select a color");
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wishImgView){
            if (wishflag == 0) {
                if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                    AddWishlist();
                    wishImgView.setImageResource(R.drawable.ic_wishlistfill);
                } else {
                    BasicFunction.showSnackbar(ProductDetailsActivity.this, "No internet connection,Please try again..!!");
                }
            }else {
                if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                    RemoveWishlist();
                    wishImgView.setImageResource(R.drawable.ic_wishlist);
                } else {
                    BasicFunction.showSnackbar(ProductDetailsActivity.this, "No internet connection,Please try again..!!");
                }
            }
        }else if (view.getId() == R.id.minusImgView){
            if (qtyTextView.getText().toString().trim().equalsIgnoreCase("1")) {
                minusImgView.setClickable(false);
            } else {
                int qty = Integer.parseInt(qtyTextView.getText().toString().trim());
                qty--;
                minusImgView.setClickable(true);
                qtyTextView.setText(""+qty);
            }
        }else if (view.getId() == R.id.plusImgView){
            int qty = Integer.parseInt(qtyTextView.getText().toString().trim());
            qty++;
            minusImgView.setClickable(true);
            qtyTextView.setText("" + qty);
        }else if (view.getId() == R.id.addCartTextView){
            if (productResponseList.get(pos).getSize().size() != 0 && productResponseList.get(pos).getColor().size() != 0){
                getSizeColor();
                if (sizeId.length() != 0 && colorId.length() != 0) {
                    AddToCart();
                }
            }else if (productResponseList.get(pos).getSize().size() != 0){
                if (sizePosList.contains(1)) {
                    for (int i = 0; i < sizePosList.size(); i++) {
                        if (sizePosList.get(i) == 1) {
                            sizeId = productResponseList.get(pos).getSize().get(i).getSize();
                        }
                    }
                    AddToCart();
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Please select a size");
                }
            }else if (productResponseList.get(pos).getColor().size() != 0){
                if (colorPosList.contains(1)) {
                    for (int i = 0; i < colorPosList.size(); i++) {
                        if (colorPosList.get(i) == 1) {
                            colorId = productResponseList.get(pos).getColor().get(i).getId();
                        }
                    }
                    AddToCart();
                }else {
                    BasicFunction.showToast(getApplicationContext(),"Please select a color");
                }
            }else {
                AddToCart();
            }
        }
    }

    @Override
    public void onSizeItemClick(View view, int position) {
        if (sizePosList.get(position) == 1){
            sizePosList.set(position, 0);
            sizeAdapter.updateSize(sizePosList);
        }else {
            for (int i = 0; i < sizePosList.size(); i++) {
                sizePosList.set(i,0);
            }
            sizePosList.set(position, 1);
            sizeAdapter.updateSize(sizePosList);
        }
    }

    @Override
    public void onColorItemClick(View view, int position) {
        if (colorPosList.get(position) == 1){
            colorPosList.set(position, 0);
            colorAdapter.updateColor(colorPosList);
        }else {
            for (int i = 0; i < colorPosList.size(); i++) {
                colorPosList.set(i,0);
            }
            colorPosList.set(position, 1);
            colorAdapter.updateColor(colorPosList);
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

            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);

        menuItem.setActionView(R.layout.actionbarlayout);

        View actionView = MenuItemCompat.getActionView(menuItem);

        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(view -> onOptionsItemSelected(menuItem));

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
