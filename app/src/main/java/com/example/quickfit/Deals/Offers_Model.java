package com.example.quickfit.Deals;

import android.widget.ImageView;

public class Offers_Model {
    private int offerId;
    private String nameOfUser;
    private String brandName;
    private String serviceName;
    private String offerDetails;
    private String validityTime;
    private String imageUrl;

    public Offers_Model(int offerId, String nameOfUser, String brandName, String serviceName, String offerDetails, String validityTime, String userImage) {
        this.offerId = offerId;
        this.nameOfUser = nameOfUser;
        this.brandName = brandName;
        this.serviceName = serviceName;
        this.offerDetails = offerDetails;
        this.validityTime = validityTime;
        this.imageUrl = userImage;
    }

    public int getOfferId() {
        return offerId;
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

    public String getOfferDetails() {
        return offerDetails;
    }

    public String getValidityTime() {
        return validityTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
