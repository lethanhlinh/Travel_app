package com.example.travel_app.Domain;

import java.io.Serializable;
import java.util.Date;

public class Gift implements Serializable {
    private String Coupon;
    private String CreatedDate;
    private String Description;
    private String ExpireDate;
    private String GiftCode;
    private String Pic;
    private double subPoint;



    public String getGiftCode() {
        return GiftCode;
    }

    public void setGiftCode(String giftCode) {
        GiftCode = giftCode;
    }

    public String getCoupon() {
        return Coupon;
    }

    public void setCoupon(String coupon) {
        Coupon = coupon;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }
    public double getSubPoint() {
        return subPoint;
    }
    public void setSubPoint(double subPoint) {
        this.subPoint = subPoint;
    }
}
