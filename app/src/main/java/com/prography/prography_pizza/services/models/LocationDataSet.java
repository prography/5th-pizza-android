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

    public static final int VEL_LEVEL0 = 0;
    public static final int VEL_LEVEL1 = 1;
    public static final int VEL_LEVEL2 = 2;
    public static final int VEL_LEVEL3 = 3;

    @PrimaryKey(autoGenerate = true)
    public int id = 0;
    @Ignore
    public ArrayList<LatLng> locations = new ArrayList<>();
    public float totalDistance = 0.0f;
    public float increaseDistance = 0.0f;
    public float velocity = 0.0f;
    public float velocityAvg = 0.0f;
    public int velocityLevel = VEL_LEVEL0;
    public int totalTime = 0;
    @Ignore
    public HashMap<Integer, Expression> changePowerIdxs = new HashMap<>(); // List Of HashMap<int changeIdx, int curColor>

    public LocationDataSet() {
    }


    protected LocationDataSet(Parcel in) {
        id = in.readInt();
        locations = in.createTypedArrayList(LatLng.CREATOR);
        totalDistance = in.readFloat();
        increaseDistance = in.readFloat();
        velocity = in.readFloat();
        velocityAvg = in.readFloat();
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
        dest.writeFloat(velocity);
        dest.writeFloat(velocityAvg);
        dest.writeInt(totalTime);
    }
}
