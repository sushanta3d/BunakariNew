package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList {

    @SerializedName("category_record")
    private List<HomePageResponse> cateResponseList;

    public List<HomePageResponse> getCateResponseList() {
        return cateResponseList;
    }

    public void setCateResponseList(List<HomePageResponse> cateResponseList) {
        this.cateResponseList = cateResponseList;
    }
}
