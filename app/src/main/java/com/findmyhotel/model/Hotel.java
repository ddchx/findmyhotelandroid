package com.findmyhotel.model;

/**
 * Created by Chathuranga on 11/27/15.
 */
public class Hotel {
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

    public Double getLatitude() {
        return lat;
    }

    public void setLatitude(Double latitude) {
        lat = latitude;
    }

    public Double getLongitude() {
        return lng;
    }

    public void setLongitude(Double longitude) {
        this.lng = longitude;
    }

    private String name;
    private Double lng;
    private Double lat;
    private String address;

    @Override
    public String toString() {
        return name + ", " + address;
    }
}
