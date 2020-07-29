package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransList {

    @SerializedName("trans_record")
    private List<TransactionResponse> transResponseList;

    public List<TransactionResponse> getTransResponseList() {
        return transResponseList;
    }

    public void setTransResponseList(List<TransactionResponse> transResponseList) {
        this.transResponseList = transResponseList;
    }
}
