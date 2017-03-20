package com.hugsby.shoppingapp.Models;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLog() {
        return lng;
    }
}
