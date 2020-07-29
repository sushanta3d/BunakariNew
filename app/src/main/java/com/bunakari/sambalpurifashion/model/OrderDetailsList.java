package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsList {

    @SerializedName("orderproducts_record")
    public List<OrderDetailsResponse> orderDetailsResponseList;

    public List<OrderDetailsResponse> getOrderDetailsResponseList() {
        return orderDetailsResponseList;
    }

    public void setOrderDetailsResponseList(List<OrderDetailsResponse> orderDetailsResponseList) {
        this.orderDetailsResponseList = orderDetailsResponseList;
    }
}
