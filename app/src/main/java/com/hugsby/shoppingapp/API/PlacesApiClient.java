package com.hugsby.shoppingapp.API;


import com.hugsby.shoppingapp.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlacesApiClient {
    public static final String BASE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    public static final String PLACES_API_KEY = BuildConfig.PLACES_API_KEY;
    private static Retrofit retrofit = null;



    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_PLACES_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
