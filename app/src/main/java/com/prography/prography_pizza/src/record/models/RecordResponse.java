package com.prography.prography_pizza.src.record.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecordResponse {
    @SerializedName("data")
    ArrayList<Data> data = new ArrayList<>();

    public static class Data {
        @SerializedName("id")
        private int recordId;
        @SerializedName("running_time")
        private double runningTime;
        @SerializedName("distance")
        private double totalDistance;
        @SerializedName("created_at")
        private long createdAt;

        public int getRecordId() {
            return recordId;
        }

        public double getRunningTime() {
            return runningTime;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public long getCreatedAt() {
            return createdAt;
        }
    }


    public ArrayList<Data> getData() {
        return data;
    }
}
