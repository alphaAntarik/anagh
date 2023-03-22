package com.etechnie.anagh.models.subscription_model;

import java.io.Serializable;

public class SubscriptionModel implements Serializable {
    private Integer id;
    private String title;
    private Integer amount;
    private Integer days;
    private String created_date;
    private Integer status;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
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
}
