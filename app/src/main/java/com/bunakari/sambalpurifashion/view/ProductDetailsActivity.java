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
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.adapter.CustomSwipeAdapter;
import com.bunakari.sambalpurifashion.model.BasicFunction;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CartResponse;
import com.bunakari.sambalpurifashion.model.GetPrefs;
import com.bunakari.sambalpurifashion.model.HeightWrapperViewPager;
import com.bunakari.sambalpurifashion.model.ProductList;
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

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener  {

    private ArrayList<ProductResponse> productResponseList = new ArrayList<>();
    private ArrayList<ProductResponse> relproductResponseList = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();
    private RecyclerView colorRecyclerView,sizeRecyclerView,prdRecyclerView;;
    private HeightWrapperViewPager viewPager;
    private ProgressBar progressBar;
    private ImageView prevImgview,nextImgview;
    private LinearLayout qtyLayout,inchesLayout;
    private ImageView shareImgView,wishImgView,plusImgView,minusImgView,likeImageView;
    private WebView descriptionWebView,disclaimerWebView;
    private Button btntotalvalue;
    private EditText txtlength,txtwidth;
    private TextView txttSize,totalprice,watermark,pNameTextView,priceTextView,offerPriceTextView,percentTextView,qtyTextView,addCartTextView,textCartItemCount,descTitleTextView,disclaimTitleTextView,likecount,productid,disclamaiertattoo;
    private int pos = 0, total, bookingamount=0,tSize=0;
    private int wishflag = 0,likeflag=0;
    private ArrayList<Integer> sizePosList = new ArrayList<>();
    private ArrayList<Integer> colorPosList = new ArrayList<>();
    private ArrayList<String> imagePosList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String mobileString,uidString,sizeId = "",colorId = "",imageName,cId,catName;
    private int cartcount = 0;
    private CardView sizeCardView,colorCardView;
 //   private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<Integer> wishflag1 = new ArrayList<>();
    private ArrayList<Integer> likeflag1= new ArrayList<>();
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    HomeProductAdapter homeProductAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Product Details");
        initUi();
        getDesignImage();
        AddToTempNotification();
        setViewData();
        GetRelProductData();

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
        disclamaiertattoo = findViewById(R.id.disclaimTitleTattoo);
        descriptionWebView = findViewById(R.id.descWebView);
        disclaimerWebView = findViewById(R.id.disclaimWebView);
        descTitleTextView = findViewById(R.id.descTitleTextView);
        disclaimTitleTextView = findViewById(R.id.disclaimTitleTextView);
        qtyLayout = findViewById(R.id.qtyLayout);
        pNameTextView = findViewById(R.id.pNameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        offerPriceTextView = findViewById(R.id.offerPriceTextView);
        percentTextView = findViewById(R.id.percentTextView);
        qtyTextView = findViewById(R.id.qtyTextView);
        addCartTextView = findViewById(R.id.addCartTextView);
        progressBar = findViewById(R.id.progressBar);
        likeImageView =  findViewById(R.id.like);
        likecount = findViewById(R.id.likscount);
        productid = findViewById(R.id.productid);
        watermark= findViewById(R.id.watermark);
        btntotalvalue = findViewById(R.id.btntotalvalue);
        totalprice= findViewById(R.id.totalprice);
        txtlength = findViewById(R.id.txtlength);
        txtwidth= findViewById(R.id.txtwidth);
        txttSize = findViewById(R.id.txttSize);
        inchesLayout = findViewById(R.id.inchesLayout);
        prdRecyclerView = findViewById(R.id.productRecyclerView);
        productResponseList = (ArrayList) getIntent().getSerializableExtra("datalist");
        pos = getIntent().getIntExtra("position",0);

        wishImgView.setOnClickListener(this);
        minusImgView.setOnClickListener(this);
        plusImgView.setOnClickListener(this);
        addCartTextView.setOnClickListener(this);
        btntotalvalue.setOnClickListener(this);

      //  mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        sharedPreferences = getSharedPreferences(GetPrefs.PREFS_NAME,getApplicationContext().MODE_PRIVATE);
        mobileString = sharedPreferences.getString(GetPrefs.PREFS_MOBILE,"");
        uidString = sharedPreferences.getString(GetPrefs.PREFS_UID,"");

        final StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    private void setViewData(){

        wishflag = productResponseList.get(pos).getInwishlist();
        likeflag = productResponseList.get(pos).getInlikelist();
        if (wishflag == 1){
            wishImgView.setImageResource(R.drawable.ic_wishlistfill);
        }else {
            wishImgView.setImageResource(R.drawable.ic_wishlist);
        }
        if (productResponseList.get(pos).getLikecount() != 0) {
            likecount.setText(productResponseList.get(pos).getLikecount()+ " Likes ");
        }
        else {

            likecount.setText("Like");
        }


        if (likeflag == 0){
            likeImageView.setImageResource(R.drawable.thumbupblack);

        }else {
             likeImageView.setImageResource(R.drawable.thumbupwhite);
        }

/*        for (int i = 0; i < productResponseList.get(pos).getSize().size(); i++) {
            sizePosList.add(i,0);
        }

        for (int i = 0; i < productResponseList.get(pos).getColor().size(); i++) {
            colorPosList.add(i,0);
        }
*/


        prdRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        prdRecyclerView.setNestedScrollingEnabled(false);
     cId = productResponseList.get(pos).getCategoryid();
        productid.setText("MA00"+productResponseList.get(pos).getId());

        pNameTextView.setText(productResponseList.get(pos).getProname());

        if (productResponseList.get(pos).getOffer_price().length() != 0) {
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
        if(cId.equalsIgnoreCase("2"))
        {
            disclamaiertattoo.setVisibility(View.VISIBLE);
            disclaimTitleTextView.setVisibility(View.GONE);
            watermark.setVisibility(View.GONE);
            qtyLayout.setVisibility(View.GONE);
            inchesLayout.setVisibility(View.VISIBLE);
            addCartTextView.setText("Book Appointment");
            getTotalTattooPrice();
            txtlength.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") ) {
                        //do your work here
                        getTotalTattooPrice();
                    }
                }



                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });

            txtwidth.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") ) {
                        //do your work here
                        getTotalTattooPrice();
                    }
                }



                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });

        }
        else {
            disclamaiertattoo.setVisibility(View.GONE);
            disclaimTitleTextView.setVisibility(View.VISIBLE);
            inchesLayout.setVisibility(View.GONE);
            addCartTextView.setText("Add to Cart");
            getTotalAmount();
        }


/*
        if(productResponseList.get(pos).getImg().length() != 59){
            imgList.add(productResponseList.get(pos).getImg().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg1().length() != 59){
            imgList.add(productResponseList.get(pos).getImg1().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg2().length() != 59){
            imgList.add(productResponseList.get(pos).getImg2().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg3().length() != 59){
            imgList.add(productResponseList.get(pos).getImg3().replaceAll(" ","%20"));
        }

        if(productResponseList.get(pos).getImg4().length() != 59){
            imgList.add(productResponseList.get(pos).getImg4().replaceAll(" ","%20"));
        }
*/
        viewPager.setAdapter(new CustomSwipeAdapter(getApplicationContext(),imgList){
            public Object instantiateItem(ViewGroup container, final int position) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View swipe_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
                ImageView imageView = (ImageView)swipe_view.findViewById(R.id.image_view);
                final ProgressBar progressBar = (ProgressBar)swipe_view.findViewById(R.id.progressBar);

                BasicFunction.showImage(imgList.get(position),getApplicationContext(),imageView,progressBar);
               String imgurl = imgList.get(position);
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

                shareImgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView proImgView = (ImageView)viewPager.findViewWithTag(viewPager.getCurrentItem());
                        proImgView.setDrawingCacheEnabled(true);

                        Uri bmpUri = getLocalBitmapUri(proImgView);
                        if (bmpUri != null) {
                            Spanned spanned = Html.fromHtml(Html.fromHtml(productResponseList.get(pos).getDescription()).toString());
                            String boldHeader = "*Product Enquiry*";

                            // Construct a ShareIntent with link to image
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, boldHeader + " : MA00 " +productResponseList.get(pos).getId() +"\n\n"+"*Product Name:* " + productResponseList.get(pos).getProname() + "\n\n" + "*Description:* \n\n" + spanned + "\n\n" + "* Click Below Link to View:* \n\n" + "https://play.google.com/store/apps/details?id=com.marriagearts.mehndi" + productResponseList.get(pos).getUrl() );
                          //  shareIntent.setType("image*//*");
                             shareIntent.setType("text/plain");

                            // Launch sharing dialog for image
                            startActivity(Intent.createChooser(shareIntent, "Share Product " + productResponseList.get(pos).getProname()));
                        } else {
                            Toast.makeText(getApplicationContext(), "Not able to share this product...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                container.addView(swipe_view);
                return swipe_view;
            }
        });



        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (likeflag == 0) {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        AddLikelist();
                        likeImageView.setImageResource(R.drawable.thumbupwhite);
                    } else {
                        BasicFunction.showSnackbar(ProductDetailsActivity.this, "No internet connection,Please try again..!!");
                    }
                }else {
                    if (BasicFunction.isNetworkAvailable(getApplicationContext())) {
                        RemoveLikelist();
                        likeImageView.setImageResource(R.drawable.thumbupblack);
                    } else {
                        BasicFunction.showSnackbar(ProductDetailsActivity.this, "No internet connection,Please try again..!!");
                    }
                }

                //Toast.makeText(getApplicationContext(), "Not able to share this product...", Toast.LENGTH_LONG).show();
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


    }



    private void AddToTempNotification(){
     //   progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addtoTempNoti(productResponseList.get(pos).getId(),mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
             //   progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        //wishflag = 1;
                    //    BasicFunction.showToast(getApplicationContext(),"Item added ");
                    }else {
                      ///  wishImgView.setImageResource(R.drawable.ic_wishlist);
                   //     BasicFunction.showToast(getApplicationContext(),"Item can't added");
                    }
                }else {
                  //  wishImgView.setImageResource(R.drawable.ic_wishlist);
                 //   BasicFunction.showToast(getApplicationContext(),"Item can't added");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
              //  progressBar.setVisibility(View.GONE);
              //  wishImgView.setImageResource(R.drawable.ic_wishlist);
           //     BasicFunction.showToast(getApplicationContext(),"Item can't added to wishlist1");
            }
        });
    }

    private void AddLikelist(){
     //   progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.addLikelist(productResponseList.get(pos).getId(),mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
       //         progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        likeflag = 1;
                        BasicFunction.showToast(getApplicationContext(),"Liked");
                    }else {
                        likeImageView.setImageResource(R.drawable.thumbupblack);
                        BasicFunction.showToast(getApplicationContext(),"Item can't added ");
                    }
                }else {
                    likeImageView.setImageResource(R.drawable.thumbupblack);
                    BasicFunction.showToast(getApplicationContext(),"Item can't added ");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                likeImageView.setImageResource(R.drawable.thumbupblack);
                BasicFunction.showToast(getApplicationContext(),"Item can't added ");
            }
        });
    }



    private void AddWishlist(){
    //    progressBar.setVisibility(View.VISIBLE);
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

    private void RemoveLikelist(){
     //   progressBar.setVisibility(View.VISIBLE);
        ApiService addService = RetroClass.getApiService();
        Call<SignupResponse> addResponseCall = addService.removeLikelist(productResponseList.get(pos).getId(),mobileString);
        addResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                progressBar.setVisibility(View.GONE);
                SignupResponse signupResponse = response.body();
                if (signupResponse != null) {
                    if (signupResponse.success == 1){
                        likeflag = 0;
                        BasicFunction.showToast(getApplicationContext(),"Item remove from likelist");
                    }else {
                        likeImageView.setImageResource(R.drawable.thumbupwhite);
                        BasicFunction.showToast(getApplicationContext(),"Item can't remove from likelist");
                    }
                }else {
                    likeImageView.setImageResource(R.drawable.thumbupwhite);
                    BasicFunction.showToast(getApplicationContext(),"Item can't remove from likelist");
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                likeImageView.setImageResource(R.drawable.thumbupwhite);
                BasicFunction.showToast(getApplicationContext(),"Item can't remove from likelist");
            }
        });
    }


    private void RemoveWishlist(){
      //  progressBar.setVisibility(View.VISIBLE);
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
   //     progressBar.setVisibility(View.VISIBLE);
        getTotalAmount();
        ApiService addCartService = RetroClass.getApiService();
        Call<SignupResponse> cartResponseCall = addCartService.addToCart(uidString,
                productResponseList.get(pos).getId(),bookingamount+"",qtyTextView.getText().toString(),total+"");
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
        int bamt=0;

        if (productResponseList.get(pos).getOffer_price().length() != 0) {
            total = total + (Integer.parseInt(productResponseList.get(pos).getOffer_price()) * Integer.parseInt(qtyTextView.getText().toString()));
            bookingamount = total*20/100;
            totalprice.setText("\u20B9 "+ String.valueOf(bookingamount));
        }else {
            total = total + (Integer.parseInt(productResponseList.get(pos).getPrice()) * Integer.parseInt(qtyTextView.getText().toString()));
            bookingamount = total*20/100;

            totalprice.setText("\u20B9 "+String.valueOf(bookingamount));
        }


    }



    private void getTotalTattooPrice(){

    String a = txtlength.getText().toString();
    String b = txtwidth.getText().toString();
        if (txtwidth.length()!=0 && txtlength.length()!=0){

            tSize =Integer.parseInt(a) * Integer.parseInt(b);
            String t = String.valueOf((tSize));
            txttSize.setText( t + " Inches");
            bookingamount = tSize * Integer.parseInt(productResponseList.get(pos).getOffer_price());
          totalprice.setText("\u20B9 "+String.valueOf(bookingamount));
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


    private void getDesignImage(){
        Log.d("DesignLength",productResponseList.get(pos).getSc().size()+" ");
        if (productResponseList.get(pos).getSc().size() > 0) {

                for (int i = 0; i < productResponseList.get(pos).getSc().size(); i++) {

                    imageName = productResponseList.get(pos).getSc().get(i).getImg();
                        Log.d("ImageName", imageName);
                    imgList.add(productResponseList.get(pos).getSc().get(i).getImg().replaceAll(" ","%20"));
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
        }

        else if (view.getId() == R.id.addCartTextView){

            if(cId.equalsIgnoreCase("2")) {
                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                intent.putExtra("amount", String.valueOf(bookingamount));
                intent.putExtra("img", productResponseList.get(pos).getImg());
                intent.putExtra("pName", productResponseList.get(pos).getProname());
                intent.putExtra("tSize", tSize);
                intent.putExtra("uId", uidString);
                intent.putExtra("cId", cId);
                intent.putExtra("pId", productResponseList.get(pos).getId());
                intent.putExtra("bookingamt","1000");

                startActivity(intent);
            }
            else{
                AddToCart();
            }
          }

        else if(view.getId()==R.id.btntotalvalue){
            getTotalTattooPrice();
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
            case R.id.play:
                Intent vintent = new Intent(getApplicationContext(),VideosActivity.class);
                startActivity(vintent);
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



    private void GetRelProductData() {
        ApiService productService = RetroClass.getApiService();
        Call<ProductList> prodServiceList = productService.getRelProducts(cId,mobileString);

        prodServiceList.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                progressBar.setVisibility(View.GONE);
                relproductResponseList = new ArrayList<>();
                try {
                    List<ProductResponse> pageResponses = null;
                    if (response.body() != null) {
                        pageResponses = response.body().getProductResponseList();
                        relproductResponseList.addAll(pageResponses);

                        if (relproductResponseList.size() > 0) {
                            wishflag1 = new ArrayList<>();
                            likeflag1= new ArrayList<>();
                            for (int i = 0; i < relproductResponseList.size(); i++) {
                                wishflag1.add(0);
                                likeflag1.add(0);
                            }

                            for (int i = 0; i < relproductResponseList.size(); i++) {
                                wishflag1.set(i,relproductResponseList.get(i).getInwishlist());
                                likeflag1.set(i,relproductResponseList.get(i).getInlikelist());
                            }


                            // Create the recyclerViewAdapter
                            homeProductAdapter = new HomeProductAdapter(getApplicationContext(), relproductResponseList, wishflag1, likeflag1, new HomeProductAdapter.getAdapterItemLcick() {
                                @Override
                                public void getItemClick(int position, ProductResponse response) {
                                    // Toast.makeText( getActivity(), productResponseList.get(position).getId(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext() ,ProductDetailsActivity.class);
                                    intent.putExtra("datalist", relproductResponseList);
                                    intent.putExtra("position", position);
                                    intent.putExtra("cId",  relproductResponseList.get(position).getCategoryid());
                                    intent.putExtra("catName",  relproductResponseList.get(position).getCategory());
                                    startActivity(intent);
                                    Bundle bundle = new Bundle();


                                }

                                @Override
                                public void getLikeClick(int position, ProductResponse response, ImageView imageView) {
                                    homeProductAdapter.AddLikelist(getApplicationContext(),response.getId(),mobileString,imageView);
                                }
                            });
                            prdRecyclerView.setAdapter(homeProductAdapter);

                            //      prdRecyclerView.setLayoutManager(gridLayoutManager);
                        } else {
                            //notTextView.setVisibility(View.VISIBLE);
                        //    notTextView.setText("Something Went Wrong");
                        }
                    }else {
                   //     notTextView.setVisibility(View.VISIBLE);
                   //     notTextView.setText("Sorry, No Data Found");
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                 //   notTextView.setVisibility(View.VISIBLE);
                 //   notTextView.setText("Sorry, No Data Found");
                }

            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
             //   notTextView.setVisibility(View.VISIBLE);
             //   notTextView.setText("Sorry, No Data Found");
            }
        });
    }
}
