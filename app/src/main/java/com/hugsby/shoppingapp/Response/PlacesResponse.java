package com.hugsby.shoppingapp.Response;

import com.google.gson.annotations.SerializedName;
import com.hugsby.shoppingapp.Models.Place;

import java.util.List;


public class PlacesResponse {
    @SerializedName("results")
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }
}
