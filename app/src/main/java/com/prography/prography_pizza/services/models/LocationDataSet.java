package com.prography.prography_pizza.services.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.style.expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
public class LocationDataSet implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    @Ignore
    public ArrayList<LatLng> locations = new ArrayList<>(); // List of LatLngs
    public float totalDistance = 0.0f;
    public float increaseDistance = 0.0f;
    public float speedAvg = 0.0f;
    public int totalTime = 0;
    @Ignore
    public ArrayList<Float> speeds = new ArrayList<>(); // List Of Velocity
    public LocationDataSet() {
    }


    protected LocationDataSet(Parcel in) {
        id = in.readInt();
        locations = in.createTypedArrayList(LatLng.CREATOR);
        totalDistance = in.readFloat();
        increaseDistance = in.readFloat();
        speedAvg = in.readFloat();
        totalTime = in.readInt();
    }

    public static final Creator<LocationDataSet> CREATOR = new Creator<LocationDataSet>() {
        @Override
        public LocationDataSet createFromParcel(Parcel in) {
            return new LocationDataSet(in);
        }

        @Override
        public LocationDataSet[] newArray(int size) {
            return new LocationDataSet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(locations);
        dest.writeFloat(totalDistance);
        dest.writeFloat(increaseDistance);
        dest.writeFloat(speedAvg);
        dest.writeInt(totalTime);
    }
}
