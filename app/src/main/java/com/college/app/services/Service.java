package com.college.app.services;

public class Service {
    String serviceName, serviceShortDesc;
    String link;


    public Service() {

    }

    public String getServiceShortDesc() {
        return serviceShortDesc;
    }

    public void setServiceShortDesc(String serviceShortDesc) {
        this.serviceShortDesc = serviceShortDesc;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
