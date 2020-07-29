package com.bunakari.sambalpurifashion.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePageResponse implements Serializable {

    //Banner
    public String banner;
    public  String name;

    //category
    public String category;
    public String catebanner;
    public String status;
    public String cid;

    public ArrayList<SubCategoryResponse> sc;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCatebanner() {
        return catebanner;
    }

    public void setCatebanner(String catebanner) {
        this.catebanner = catebanner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public ArrayList<SubCategoryResponse> getSc() {
        return sc;
    }

    public void setSc(ArrayList<SubCategoryResponse> sc) {
        this.sc = sc;
    }
}
