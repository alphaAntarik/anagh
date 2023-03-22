package com.etechnie.anagh.models.slot_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateSlot {

    @SerializedName("day")
    @Expose
    private int day;

    @SerializedName("weekday")
    @Expose
    private String weekday;

    @SerializedName("month")
    @Expose
    private String month;

    @SerializedName("data")
    @Expose
    private String data;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
