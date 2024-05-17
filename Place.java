package com.example.placesapi2;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    private String name;
    private String address;
    private LatLng latLng;
    private String photoUrl;

    public Place() {

    }

    public Place(String name, String address, com.google.android.gms.maps.model.LatLng latLng) {
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public com.google.android.gms.maps.model.LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        this.latLng = latLng;
    }
    public String getIconUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
