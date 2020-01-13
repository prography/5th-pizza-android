package com.prography.prography_pizza.src.record.models;

import com.google.gson.annotations.SerializedName;

public class RecordRequest {
    @SerializedName("challenge_id")
    private int challengeId;
    @SerializedName("running_time")
    private double totalTime;
    @SerializedName("distance")
    private double distance;
    @SerializedName("screenshot")
    private String latLngs;

    public RecordRequest(int challengeId, double totalTime, double distance, String latLngs) {
        this.challengeId = challengeId;
        this.totalTime = totalTime;
        this.distance = distance;
        this.latLngs = latLngs;
    }
}
