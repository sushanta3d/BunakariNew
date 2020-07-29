package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {

    @SerializedName("product_record")
    private List<VideoResponse> productResponseList;

    public List<VideoResponse> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<VideoResponse> productResponseList) {
        this.productResponseList = productResponseList;
    }

    public boolean has_more;

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }
}
