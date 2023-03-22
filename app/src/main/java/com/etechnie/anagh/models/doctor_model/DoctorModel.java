package com.etechnie.anagh.models.doctor_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DoctorModel implements Serializable {
    private Integer id;
    private String icon;
    private String image;
    private String name;
    private String description;
    private String experience;
    private String address;
    private String fees;
    private String lat;
    private String lng;
    private String speciality;
    private String createdDate;
    private Integer status;
    private String degree;
    private String chamberType;
    private String clinic;
    @SerializedName("clinic_address")
    @Expose
    private String clinicAddress;
    private String clinicImage;

    @SerializedName("clinic_description")
    @Expose
    private String clinicDescription;

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    private String contact_no;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
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
    public String getExperience() {
        return experience;
    }
    public void setExperience(String experience) {
        this.experience = experience;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getFees() {
        return fees;
    }
    public void setFees(String fees) {
        this.fees = fees;
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
    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getDegree() {
        return degree;
    }
    public void setDegree(String degree) {
        this.degree = degree;
    }
    public String getChamberType() {
        return chamberType;
    }
    public void setChamberType(String chamberType) {
        this.chamberType = chamberType;
    }
    public String getClinic() {
        return clinic;
    }
    public void setClinic(String clinic) {
        this.clinic = clinic;
    }
    public String getClinicAddress() {
        return clinicAddress;
    }
    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }
    public String getClinicImage() {
        return clinicImage;
    }
    public void setClinicImage(String clinicImage) {
        this.clinicImage = clinicImage;
    }
    public String getClinicDescription() {
        return clinicDescription;
    }
    public void setClinicDescription(String clinicDescription) {
        this.clinicDescription = clinicDescription;
    }
}
