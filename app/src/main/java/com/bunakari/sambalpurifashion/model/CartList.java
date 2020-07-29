package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartList {

    @SerializedName("cart_record")
    private List<CartResponse> cartResponseList;

    public List<CartResponse> getCartResponseList() {
        return cartResponseList;
    }

    public void setCartResponseList(List<CartResponse> cartResponseList) {
        this.cartResponseList = cartResponseList;
    }
}
