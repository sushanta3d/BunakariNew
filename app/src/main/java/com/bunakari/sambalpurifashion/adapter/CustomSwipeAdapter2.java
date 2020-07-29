package com.bunakari.sambalpurifashion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.TouchImageView;


import java.util.ArrayList;


/**
 * Created by admin on 7/10/2017.
 */

public class CustomSwipeAdapter2 extends PagerAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;

    ArrayList<String> images = new ArrayList<>();

    public CustomSwipeAdapter2(Activity activity, Context context, ArrayList<String> images) {
        this.activity = activity;
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (ConstraintLayout) object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View swipe_view = layoutInflater.inflate(R.layout.zoom_pager, container, false);
       // TouchImageView imageView = swipe_view.findViewById(R.id.touchimageview1);
        ImageView crossImageView = swipe_view.findViewById(R.id.btnClose);
        TouchImageView imageFullScreen = swipe_view.findViewById(R.id.imageFullScreen);
        Glide.with(context).load(images.get(position)).into(imageFullScreen);

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });

        container.addView(swipe_view);
        return swipe_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
