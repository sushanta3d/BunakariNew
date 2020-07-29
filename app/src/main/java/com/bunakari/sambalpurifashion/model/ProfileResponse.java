package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    public int success;

    public String img;
    public String name;
    public String mob;
    public String email;
    public String dob;
    public String address;
    public String state;
    public String pincode;
    public String dpamount;
    public String mybalance;
    public String mynetwork;

    @SerializedName("userid")
    public String referalid;

    public String appreferralcode;

    public String earnamount;

    public String returnamount;

    public String money;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDpamount() {
        return dpamount;
    }

    public void setDpamount(String dpamount) {
        this.dpamount = dpamount;
    }

    public String getReferalid() {
        return referalid;
    }

    public void setReferalid(String referalid) {
        this.referalid = referalid;
    }

    public String getMybalance() {
        return mybalance;
    }

    public void setMybalance(String mybalance) {
        this.mybalance = mybalance;
    }

    public String getMynetwork() {
        return mynetwork;
    }

    public void setMynetwork(String mynetwork) {
        this.mynetwork = mynetwork;
    }

    public String getAppreferralcode() {
        return appreferralcode;
    }

    public void setAppreferralcode(String appreferralcode) {
        this.appreferralcode = appreferralcode;
    }

    public String getEarnamount() {
        return earnamount;
    }

    public void setEarnamount(String earnamount) {
        this.earnamount = earnamount;
    }

    public String getReturnamount() {
        return returnamount;
    }

    public void setReturnamount(String returnamount) {
        this.returnamount = returnamount;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
