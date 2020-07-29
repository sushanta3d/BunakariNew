package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressList {

    @SerializedName("category_record")
    private List<AddressResponse> addressResponseList;

    public List<AddressResponse> getAddressResponseList() {
        return addressResponseList;
    }

    public void setAddressResponseList(List<AddressResponse> addressResponseList) {
        this.addressResponseList = addressResponseList;
    }
}
