package com.example.quickfit.Services;

public class ServicesItemModel {

    private String serviceImage;
    private String serviceName;
    private int id;

    public ServicesItemModel(String serviceImage, String serviceName, int i) {
        this.serviceImage = serviceImage;
        this.serviceName = serviceName.toLowerCase();
        int id = 0;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }
}
