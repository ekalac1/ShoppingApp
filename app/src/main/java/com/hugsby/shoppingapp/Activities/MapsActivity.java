package com.hugsby.shoppingapp.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hugsby.shoppingapp.API.PlacesApiClient;
import com.hugsby.shoppingapp.API.PlacesApiInterface;
import com.hugsby.shoppingapp.Models.Place;
import com.hugsby.shoppingapp.R;
import com.hugsby.shoppingapp.Response.PlacesResponse;
import com.hugsby.shoppingapp.ShoppingApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    PlacesApiInterface apiService;
    ShoppingApplication sApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        sApp = ShoppingApplication.getInstance();
        apiService=sApp.getPlacesApiInterface();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), R.string.enable_location, Toast.LENGTH_LONG).show();
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double x = mLastLocation.getLatitude();
            double y = mLastLocation.getLongitude();
            LatLng currLocation = new LatLng(x, y);
            if (mMap!=null)
            {
                mMap.addMarker(new MarkerOptions().position(currLocation).title("Our location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currLocation.latitude, currLocation.longitude), 12.0f));
            }
            Call<PlacesResponse> call= apiService.SearchPlaces("json", PlacesApiClient.PLACES_API_KEY, String.valueOf(x)+","+String.valueOf(y), 10000, "shopping_mall");
            call.enqueue(new Callback<PlacesResponse>() {
                @Override
                public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                    for (Place p : response.body().getResults())
                    {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(p.getGeometry().getLocation().getLat(), p.getGeometry().getLocation().getLog())).title(p.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                    }
                }

                @Override
                public void onFailure(Call<PlacesResponse> call, Throwable t) {

                }
            });
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
