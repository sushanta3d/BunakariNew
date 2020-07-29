package com.bunakari.sambalpurifashion.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;


public class TattooBooking extends AppCompatActivity implements View.OnClickListener {
   private ImageView proImageView,cancelImageView,minusImageView,plusImageView;
   private   TextView proTextView,priceTextView,offerPriceTextView,sizeTitleTextView,sizeTextView,qtyTextView,colorTitleTextView;
   private   ProgressBar progressBar;

   private  String tPrice,tSize,img,pName,uId;
    CardView colorCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tattoo_booking);
        initUi();
    }
    public void initUi(){

        proImageView = findViewById(R.id.proImgView);
        cancelImageView = findViewById(R.id.cancelImgView);
        minusImageView = findViewById(R.id.minusImgView);
        plusImageView = findViewById(R.id.plusImgView);
        proTextView = findViewById(R.id.pNameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        offerPriceTextView = findViewById(R.id.offerPriceTextView);
        sizeTitleTextView = findViewById(R.id.sizeTitleTextView);
        sizeTextView = findViewById(R.id.sizeValueTextView);
        colorTitleTextView = findViewById(R.id.colorTitleTextView);
        colorCardView =findViewById(R.id.colorCardView);
        qtyTextView = findViewById(R.id.qtyTextView);
        progressBar = findViewById(R.id.progressBar);

        tPrice = getIntent().getStringExtra("amount");

        pName = getIntent().getStringExtra("pName");
        tSize = getIntent().getStringExtra("tSize");
        uId = getIntent().getStringExtra("uId");
        img = getIntent().getStringExtra("pId");
        BasicFunction.showImage(getIntent().getStringExtra("img"),getApplicationContext(),proImageView,progressBar);
        proTextView.setText(pName);
        qtyTextView.setText(getIntent().getStringExtra("tSize"));


    }

    @Override
    public void onClick(View v) {

    }
}
