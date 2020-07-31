package com.bunakari.sambalpurifashion.model;

import com.bunakari.sambalpurifashion.network.RetroClass;

import java.io.Serializable;
import java.util.List;



public class ProductResponse implements Serializable {

    private String id;
    private String proname;
    private String price;
    private String offer_price;
    private String img;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private String img6;
    private String img7;
    private String img8;
    private String img9;
    private String description;
    private String desclaimer;
    private Integer inwishlist;

    private List<ColorResponse> color;

    private List<SizeResponse> size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getImg() {
        return RetroClass.PRODUCT_PATH1 + img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg1() {
        return RetroClass.PRODUCT_PATH2 + img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return RetroClass.PRODUCT_PATH3 + img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return RetroClass.PRODUCT_PATH4 + img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return RetroClass.PRODUCT_PATH5 + img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return  RetroClass.PRODUCT_PATH6 + img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return  RetroClass.PRODUCT_PATH6 + img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String getImg7() {
        return RetroClass.PRODUCT_PATH7 + img7;
    }

    public void setImg7(String img7) {
        this.img7 = img7;
    }

    public String getImg8() {
        return RetroClass.PRODUCT_PATH8 + img8;
    }

    public void setImg8(String img8) {
        this.img8 = img8;
    }

    public String getImg9() {
        return RetroClass.PRODUCT_PATH10 + "xyz"+img9;
    }

    public void setImg9(String img9) {
        this.img9 = img9;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesclaimer() {
        return desclaimer;
    }

    public void setDesclaimer(String desclaimer) {
        this.desclaimer = desclaimer;
    }

    public Integer getInwishlist() {
        return inwishlist;
    }

    public void setInwishlist(Integer inwishlist) {
        this.inwishlist = inwishlist;
    }

    public List<ColorResponse> getColor() {
        return color;
    }

    public void setColor(List<ColorResponse> color) {
        this.color = color;
    }

    public List<SizeResponse> getSize() {
        return size;
    }

    public void setSize(List<SizeResponse> size) {
        this.size = size;
    }
}
