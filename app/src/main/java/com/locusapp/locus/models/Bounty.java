package com.locusapp.locus.models;

import android.media.Image;

public class Bounty {

    private String title, creator, hint;
    private Image picture;
    private long longitude, latitude;

    public Bounty(String title, String creator, String hint, Image picture, long longitude, long latitude) {
        this.title = title;
        this.creator = creator;
        this.hint = hint;
        this.picture = picture;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image picture) {
        this.picture = picture;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }
}
