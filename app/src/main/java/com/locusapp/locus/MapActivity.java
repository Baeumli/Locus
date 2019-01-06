package com.locusapp.locus;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.locusapp.locus.models.GeofenceTransitionsIntentService;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: SUCCESSFULLY ADDED GEOFENCE");
            }
        })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "ERROR");
                        }
                    });
        }

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Do stuff with the map
                mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng)));

                mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(10.0)
                .build());
            }
        });
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

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
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
            mapActivity.startActivity(winIntent);
            mapActivity.finish();
        }
    }

}
