package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersList {
    @SerializedName("trailer_record")
    private List<TrailerResponse> trailerList;

    public List<TrailerResponse> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(List<TrailerResponse> trailerList) {
        this.trailerList = trailerList;
    }
}
