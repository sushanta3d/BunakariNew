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
    private String description;
    private String desclaimer;
    private Integer inwishlist;
    private Integer inlikelist;
    private  Integer likecount;
    private String category;
    private String subcategory;
    private String categoryid;
    public String getCategoryid() {
        return categoryid;
    }



    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    private List<ColorResponse> color;

    private List<SizeResponse> size;
    private List<ImageResponse> sc;

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
        return RetroClass.PRODUCT_PATH1 +  img;
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

 public  List<ImageResponse> getSc(){
        return sc;
 }

 public void setSc(List<ImageResponse>sc){
        this.sc=sc;
 }


    public Integer getInlikelist() {
        return inlikelist;
    }

    public void setInlikelist(Integer inlikelist) {
        this.inlikelist = inlikelist;
    }

    public Integer getLikecount() {
        return likecount;
    }

    public void setLikecount(Integer likecount) {
        this.likecount = likecount;
    }
}