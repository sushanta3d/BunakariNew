package com.bunakari.sambalpurifashion.model;

import java.io.Serializable;

public class SubCategoryResponse implements Serializable {

    public String sid;
    public String subcategory;
    public String img;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
