package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerList {

    @SerializedName("banner_record")
    private List<HomePageResponse> bannerList;

    public List<HomePageResponse> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<HomePageResponse> bannerList) {
        this.bannerList = bannerList;
    }
}
