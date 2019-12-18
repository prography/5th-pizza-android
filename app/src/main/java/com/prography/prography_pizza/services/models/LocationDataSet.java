package com.prography.prography_pizza.services.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LocationDataSet implements Parcelable {
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
    public ArrayList<Location> locations = new ArrayList<>();
    public float totalDistance = 0.0f;
    public float increaseDistance = 0.0f;
    public float velocity = 0.0f;
    public float velocityAvg = 0.0f;
    public long totalTime = 0;
    public ArrayList<ArrayList<Integer>> powerColors = new ArrayList<>();

    public LocationDataSet() {
    }

    protected LocationDataSet(Parcel in) {
        locations = in.createTypedArrayList(Location.CREATOR);
        totalDistance = in.readFloat();
        increaseDistance = in.readFloat();
        velocity = in.readFloat();
        velocityAvg = in.readFloat();
        totalTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(locations);
        dest.writeFloat(totalDistance);
        dest.writeFloat(increaseDistance);
        dest.writeFloat(velocity);
        dest.writeFloat(velocityAvg);
        dest.writeLong(totalTime);
    }
}
