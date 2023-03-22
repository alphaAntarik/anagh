package com.etechnie.anagh.models.clinic_model;

import java.io.Serializable;

public class ClinicModel implements Serializable {
    private Integer id;
    private String image;
    private String name;
    private String description;
    private String address;
    private String created_date;
    private Integer status;
    private String lat;
    private String lng;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCreatedDate() {
        return created_date;
    }
    public void setCreatedDate(String createdDate) {
        this.created_date = createdDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
}
