package com.example.quickfit.Brands;

import android.widget.ImageView;

public class BrandItemsModel {

    private int brandImage;
    private String BrandName;

    public BrandItemsModel(int brandImage, String brandName) {
        this.brandImage = brandImage;
        BrandName = brandName.toLowerCase();
    }

    public int getBrandImage() {
        return brandImage;
    }

    public String getBrandName() {
        return BrandName;
    }
}
