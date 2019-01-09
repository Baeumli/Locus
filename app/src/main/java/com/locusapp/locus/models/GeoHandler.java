package com.locusapp.locus.models;

import android.util.Log;

import com.mapbox.geocoder.GeocoderCriteria;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geocoder.service.models.GeocoderResponse;
import com.mapbox.mapboxsdk.geometry.LatLng;

import retrofit.Response;
import retrofit.Retrofit;

public class GeoHandler {

    private static final String TAG = "GeoHandler";

    public void reverseGeocode(String token, LatLng latLng, GeoHandlerCallback geoHandlerCallback) {
        MapboxGeocoder client = new MapboxGeocoder.Builder()
                .setAccessToken(token)
                .setCoordinates(latLng.getLongitude(), latLng.getLatitude())
                .setType(GeocoderCriteria.TYPE_PLACE)
                .build();

        client.enqueue(new retrofit.Callback<GeocoderResponse>() {
            @Override
            public void onResponse(Response<GeocoderResponse> response, Retrofit retrofit) {
                if (response.body().getFeatures().size() > 0 && response.body().getFeatures() != null) {
                    String place = response.body().getFeatures().get(0).getText();
                    geoHandlerCallback.onCallback(place);
                    Log.i(TAG, "onResponse: Successfully aquired reverse geocode location");
                } else {
                    geoHandlerCallback.onCallback("N/A");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure: Unable to reverse geocode", t);
            }
        });
    }

    public interface GeoHandlerCallback {
        void onCallback(String place);
    }
}
