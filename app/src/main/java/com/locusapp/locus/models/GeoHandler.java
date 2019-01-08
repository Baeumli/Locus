package com.locusapp.locus.models;

import android.content.Context;

import com.google.type.LatLng;
import com.locusapp.locus.R;
import com.mapbox.geocoder.MapboxGeocoder;

public class GeoHandler {

    public void reverseGeocode(Context context, LatLng latLng) {
        MapboxGeocoder client = new MapboxGeocoder.Builder()
                .setAccessToken(context.getResources().getString())

    }

}
