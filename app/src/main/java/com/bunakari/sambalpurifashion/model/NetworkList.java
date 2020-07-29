package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NetworkList {

    @SerializedName("network_record")
    private List<NetworkResponse> responseList;

    public List<NetworkResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<NetworkResponse> responseList) {
        this.responseList = responseList;
    }
}
