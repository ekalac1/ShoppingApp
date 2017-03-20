package com.hugsby.shoppingapp;


import android.app.Application;

import com.hugsby.shoppingapp.API.PlacesApiClient;
import com.hugsby.shoppingapp.API.PlacesApiInterface;

public class ShoppingApplication extends Application {

    PlacesApiInterface placesApiInterface;
    private static ShoppingApplication singleton;

    @Override
    public void onCreate() {

        super.onCreate();
        placesApiInterface = PlacesApiClient.getClient().create(PlacesApiInterface.class);
        singleton = this;
    }
    public static ShoppingApplication getInstance() {
        return singleton;
    }

    public PlacesApiInterface getPlacesApiInterface() {
        return placesApiInterface;
    }

    public void setPlacesApiInterface(PlacesApiInterface placesApiInterface) {
        this.placesApiInterface = placesApiInterface;
    }
}
