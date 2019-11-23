package com.prography.progrpahy_pizza.src.record.models;

import com.google.gson.annotations.SerializedName;

public class RecordRequest {
    @SerializedName("running_time") private double totalTime;
    @SerializedName("distance") private double distance;

    public RecordRequest(double totalTime, double distance) {
        this.totalTime = totalTime;
        this.distance = distance;
    }
}
