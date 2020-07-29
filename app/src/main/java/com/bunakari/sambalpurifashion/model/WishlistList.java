package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishlistList {

    @SerializedName("wishlist_record")
    private List<ProductResponse> wishResponseList;

    public List<ProductResponse> getWishResponseList() {
        return wishResponseList;
    }

    public void setWishResponseList(List<ProductResponse> wishResponseList) {
        this.wishResponseList = wishResponseList;
    }
}
