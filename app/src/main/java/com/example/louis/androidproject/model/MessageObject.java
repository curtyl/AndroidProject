package com.example.louis.androidproject.model;

import java.util.ArrayList;

/**
 * Created by louis on 30/01/2017 for AndroidProject.
 */

public class MessageObject {
    private long timestamp;
    private CityObject city;
    private ArrayList<IaqiObject> iaqi;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public CityObject getCity() {
        return city;
    }

    public void setCity(CityObject city) {
        this.city = city;
    }

    public ArrayList<IaqiObject> getIaqi() {
        return iaqi;
    }

    public void setIaqi(ArrayList<IaqiObject> iaqi) {
        this.iaqi = iaqi;
    }
}