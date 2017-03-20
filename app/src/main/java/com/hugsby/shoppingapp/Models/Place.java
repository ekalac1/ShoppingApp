package com.hugsby.shoppingapp.Models;

import com.google.gson.annotations.SerializedName;


public class Place {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("geometry")
    private Geometry geometry;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
