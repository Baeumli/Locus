package com.locusapp.locus.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class FirebaseDAO {

    private static final String TAG = "FirebaseDAO";

    public void uploadImage(Bitmap bitmap, String imageFileName) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("/images/" + imageFileName);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: Unable to upload image to Firebase Storage", e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "onSuccess: Uploaded image to Firebase Storage");
            }
        });
    }

    public void saveBounty(Map<String, Object> bounty) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if (bounty.get("location") != null) {
            firestore.collection("Bounties")
                    .add(bounty)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document: ", e);
                        }
                    });
        }
    }

    public void getBountyDetails(String id, FirebaseDetailCallback firebaseCallback ) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Bounties")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String title = document.getString("title");
                            String hint = document.getString("hint");
                            String creator = (String) document.getString("creator");
                            double lat = document.getGeoPoint("location").getLatitude();
                            double lng = document.getGeoPoint("location").getLongitude();
                            String image = document.getString("image");
                            String message = document.getString("win_message");

                            firebaseCallback.onCallback(title, hint, creator, lat, lng, image, message);

                        }
                    }
                });
    }

    public void getBountyList(FirebaseListCallback firebaseCallback ) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Bounties")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot querySnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed.", e);
                            return;
                        }

                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<LatLng> locations = new ArrayList<>();
                        ArrayList<String> ids = new ArrayList<>();

                        titles.clear();
                        locations.clear();
                        ids.clear();

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            titles.add(document.getString("title"));
                            locations.add(new LatLng(
                                    document.getGeoPoint("location").getLatitude(),
                                    document.getGeoPoint("location").getLongitude()
                            ));
                            ids.add(document.getId());
                        }

                        firebaseCallback.onCallback(titles, locations, ids);

                    }
                });
    }

    public void getLatLng(FirebaseLatLngCallback firebaseCallback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Bounties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<LatLng> locations = new ArrayList<>();
                            ArrayList<String> titles = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                locations.add(new LatLng(
                                        documentSnapshot.getGeoPoint("location").getLatitude(),
                                        documentSnapshot.getGeoPoint("location").getLongitude()));

                                titles.add(documentSnapshot.getString("title"));

                            }

                            firebaseCallback.onCallback(locations, titles);
                        }
                    }
                });
    }


    public interface FirebaseDetailCallback {
        void onCallback(String title, String hint, String creator, double lat, double lng, String image, String message);
    }

    public interface FirebaseListCallback {
        void onCallback(ArrayList<String> titles, ArrayList<LatLng> locations, ArrayList<String> ids);
    }

    public interface FirebaseLatLngCallback {
        void onCallback(ArrayList<LatLng> locations, ArrayList<String> titles);
    }
}