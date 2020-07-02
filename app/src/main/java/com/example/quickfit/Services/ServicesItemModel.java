package com.example.quickfit.Services;

public class ServicesItemModel {

    private int serviceImage;
    private String serviceName;

    public ServicesItemModel(int serviceImage, String serviceName) {
        this.serviceImage = serviceImage;
        this.serviceName = serviceName.toLowerCase();
    }

    public int getServiceImage() {
        return serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }
}
