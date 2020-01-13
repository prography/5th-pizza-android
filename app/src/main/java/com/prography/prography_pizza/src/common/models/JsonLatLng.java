package com.prography.prography_pizza.src.common.models;

import com.google.gson.annotations.SerializedName;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.prography.prography_pizza.services.models.LocationDataSet;

import java.util.ArrayList;

public class JsonLatLng {
    public static class Data {
        @SerializedName("Lat") private double lat;
        @SerializedName("Lng") private double lng;
        @SerializedName("Alt") private double alt;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public double getAlt() {
            return alt;
        }

        public Data(double lat, double lng, double alt) {
            this.lat = lat;
            this.lng = lng;
            this.alt = alt;
        }

        public Data(LatLng latLng) {
            this.lat = latLng.getLatitude();
            this.lng = latLng.getLongitude();
            this.alt = latLng.getAltitude();
        }
    }

    @SerializedName("data")
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public JsonLatLng(LocationDataSet locationDataSet) {
        //atLng
    }
}
