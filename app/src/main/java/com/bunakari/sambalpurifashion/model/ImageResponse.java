package com.bunakari.sambalpurifashion.model;

import com.marriagearts.mehndi.network.RetroClass;

import java.io.Serializable;

public class ImageResponse implements Serializable {

    private String img;
    private String imgid;

    public String getImg() {
        return  RetroClass.PRODUCT_PATH5 + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }
}
