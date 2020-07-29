package com.bunakari.sambalpurifashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.BasicFunction;

import java.util.ArrayList;



/**
 * Created by admin on 7/10/2017.
 */

public class CustomSwipeAdapter extends PagerAdapter {


    private Context context;
    private LayoutInflater layoutInflater;

    ArrayList<String> images = new ArrayList<>();

    public CustomSwipeAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View swipe_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)swipe_view.findViewById(R.id.image_view);
        final ProgressBar progressBar = (ProgressBar)swipe_view.findViewById(R.id.progressBar);

        BasicFunction.showImage(images.get(position),context,imageView,progressBar);

        container.addView(swipe_view);
        return swipe_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
