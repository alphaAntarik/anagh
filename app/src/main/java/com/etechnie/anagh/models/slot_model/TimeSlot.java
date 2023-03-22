package com.etechnie.anagh.models.slot_model;

import java.io.Serializable;

public class TimeSlot implements Serializable {
    private  int time;
    private String timevalue;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(String timevalue) {
        this.timevalue = timevalue;
    }
}
