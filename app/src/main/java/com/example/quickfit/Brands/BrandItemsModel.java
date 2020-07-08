package com.example.quickfit.Brands;

import android.widget.ImageView;

public class BrandItemsModel {

    private String brandImage;
    private String BrandName;
    private int id;

    public BrandItemsModel(String brandImage, String brandName, int i) {
        this.brandImage = brandImage;
        BrandName = brandName;
        this.id = i;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public String getBrandName() {
        return BrandName;
    }

    public int getId() {
        return id;
    }
}
