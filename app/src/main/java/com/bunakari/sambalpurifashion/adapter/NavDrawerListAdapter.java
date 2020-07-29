package com.bunakari.sambalpurifashion.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bunakari.sambalpurifashion.R;
import com.bunakari.sambalpurifashion.model.NavDrawerItem;

import java.util.ArrayList;


public class NavDrawerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private ListView listview;
    ImageView imgIcon;
    TextView txtTitle;
    Typeface ty;
    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, ListView listview){
        this.context = context;
        this.listview = listview;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);

        }
        imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        txtTitle = (TextView) convertView.findViewById(R.id.title);
        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

}