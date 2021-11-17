package com.example.a2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.Serializable;

//Location object. Use get and set for retriving values

public class Location {

    private int id;
    private float latitude;
    private float longitude;
    private String address;

    public Location(int id, float latitude, float longitude, String address) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
