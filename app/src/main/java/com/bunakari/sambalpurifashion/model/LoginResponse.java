package com.bunakari.sambalpurifashion.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    public int success;
    public String message;
    public String name;
    public String mob;
    public String email;
    public String dob;
    public String address;
    public String state;
    public String pincode;
    public String dpamount;
    public String appreferralcode;

    @SerializedName("userid")
    public String referalid;

    @SerializedName("id")
    public String userid;

    public String earningamount;

    public String returnamount;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppreferralcode() {
        return appreferralcode;
    }

    public void setAppreferralcode(String appreferralcode) {
        this.appreferralcode = appreferralcode;
    }

    public String getEarningamount() {
        return earningamount;
    }

    public void setEarningamount(String earningamount) {
        this.earningamount = earningamount;
    }

    public String getReturnamount() {
        return returnamount;
    }

    public void setReturnamount(String returnamount) {
        this.returnamount = returnamount;
    }
}
