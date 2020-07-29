package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderList {

    @SerializedName("order_record")
    public List<OrderResponse> orderResponseList;

    public List<OrderResponse> getOrderResponseList() {
        return orderResponseList;
    }

    public void setOrderResponseList(List<OrderResponse> orderResponseList) {
        this.orderResponseList = orderResponseList;
    }
}
