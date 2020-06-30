package com.example.quickfit.Deals;

import android.widget.ImageView;

public class Offers_Model {
    private String nameOfUser;
    private String brandName;
    private String serviceName;
    private String phoneNumber;
    private String offerDetails;
    private ImageView userImage;

    public Offers_Model(String nameOfUser, String brandName, String serviceName, String phoneNumber, String offerDetails, ImageView userImage) {
        this.nameOfUser = nameOfUser;
        this.brandName = brandName;
        this.serviceName = serviceName;
        this.phoneNumber = phoneNumber;
        this.offerDetails = offerDetails;
        this.userImage = userImage;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public ImageView getUserImage() {
        return userImage;
    }
}
