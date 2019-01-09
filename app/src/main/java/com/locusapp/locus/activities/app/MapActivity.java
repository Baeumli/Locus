package com.locusapp.locus.activities.app;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.locusapp.locus.R;
import com.locusapp.locus.models.GeofenceTransitionsIntentService;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private GeofencingClient geofencingClient;
    private Geofence geofence;
    private PendingIntent pendingIntent;
    private static final String TAG = "MapActivity";
    private static ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        constraintLayout = findViewById(R.id.constraintLayout);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        GeoReceiver geoReceiver = new GeoReceiver(this);
        broadcastManager.registerReceiver(geoReceiver, new IntentFilter("geofence"));

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        double lng = getIntent().getDoubleExtra("longitude", 0.0);
        double lat = getIntent().getDoubleExtra("latitude", 0.0);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofence = new Geofence.Builder()
                .setRequestId("1")
                // lat, long, radius
                .setCircularRegion(lat, lng, 100)

                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build();

        if (geofence != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Geofence added");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Unable to add Geofence", e);
                        }
                    });
        }

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                int color = Color.parseColor("#3D348B");
                drawCircle(mapboxMap, new LatLng(lat, lng), color, 1000);

                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(10.0)
                .build());

                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationComponent.activateLocationComponent(getApplicationContext());
                locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
                locationComponent.setLocationComponentEnabled(true);
            }
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    static class GeoReceiver extends BroadcastReceiver {

        private ConstraintLayout constraintLayout;
        MapActivity mapActivity;

        public GeoReceiver(Activity activity) {
            mapActivity = (MapActivity) activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent winIntent = new Intent(mapActivity.getApplicationContext(), WinActivity.class);
            winIntent.putExtra("image", mapActivity.getIntent().getStringExtra("image"));
            winIntent.putExtra("message", mapActivity.getIntent().getStringExtra("message"));
            mapActivity.startActivity(winIntent);

            mapActivity.geofencingClient.removeGeofences(mapActivity.getGeofencePendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "onSuccess: Deleted Geofence!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Couldn't delete Geofence", e);
                        }
                    });
            mapActivity.finish();
        }
    }

    // Lifecycle methods

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    // Taken from https://stackoverflow.com/a/46702165

    public static void drawCircle(MapboxMap map, LatLng position, int color, double radiusMeters) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(color);
        polylineOptions.width(0.5f); // change the line width here
        polylineOptions.addAll(getCirclePoints(position, radiusMeters));
        map.addPolyline(polylineOptions);
    }

    private static ArrayList<LatLng> getCirclePoints(LatLng position, double radius) {
        int degreesBetweenPoints = 10; // change here for shape
        int numberOfPoints = (int) Math.floor(360 / degreesBetweenPoints);
        double distRadians = radius / 6371000.0; // earth radius in meters
        double centerLatRadians = position.getLatitude() * Math.PI / 180;
        double centerLonRadians = position.getLongitude() * Math.PI / 180;
        ArrayList<LatLng> polygons = new ArrayList<>(); // array to hold all the points
        for (int index = 0; index < numberOfPoints; index++) {
            double degrees = index * degreesBetweenPoints;
            double degreeRadians = degrees * Math.PI / 180;
            double pointLatRadians = Math.asin(sin(centerLatRadians) * cos(distRadians)
                    + cos(centerLatRadians) * sin(distRadians) * cos(degreeRadians));
            double pointLonRadians = centerLonRadians + Math.atan2(sin(degreeRadians)
                            * sin(distRadians) * cos(centerLatRadians),
                    cos(distRadians) - sin(centerLatRadians) * sin(pointLatRadians));
            double pointLat = pointLatRadians * 180 / Math.PI;
            double pointLon = pointLonRadians * 180 / Math.PI;
            LatLng point = new LatLng(pointLat, pointLon);
            polygons.add(point);
        }
        // add first point at end to close circle
        polygons.add(polygons.get(0));
        return polygons;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        geofencingClient.removeGeofences(getGeofencePendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "onSuccess: Deleted Geofence!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: Couldn't delete Geofence", e);
            }
        });
    }
}
