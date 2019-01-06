package com.locusapp.locus.models;

import android.media.Image;

import com.google.firebase.firestore.GeoPoint;

public class Bounty {

    private String title, creator, hint;
    private Image image;
    private GeoPoint location;

    public Bounty(String title, String creator, String hint, Image image, GeoPoint location) {
        this.title = title;
        this.creator = creator;
        this.hint = hint;
        this.image = image;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
