package com.bunakari.sambalpurifashion.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bunakari.sambalpurifashion.view.TabFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String title[] = {"Mehndi", "Tattoo", "Makeup"};


    public ViewPagerAdapter(FragmentManager manager) {

        super(manager);

    }

    @Override
    public Fragment getItem(int position) {

        return TabFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
