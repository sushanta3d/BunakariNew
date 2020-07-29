package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReletedVideoList {
    @SerializedName("product_record")
    private List<RelVideoResponse> relproductResponseList;

    public List<RelVideoResponse> getRelproductResponseList() {
        return relproductResponseList;
    }

    public void  setRelproductResponseList(List<RelVideoResponse> relproductResponseList) {
        this.relproductResponseList = relproductResponseList;
    }

    public boolean has_more;

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }
}
