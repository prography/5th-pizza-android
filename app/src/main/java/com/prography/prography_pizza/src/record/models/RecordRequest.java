package com.prography.prography_pizza.src.record.models;

import com.google.gson.annotations.SerializedName;

public class RecordRequest {
    @SerializedName("challenge_id")
    private int challengeId;
    @SerializedName("running_time")
    private double totalTime;
    @SerializedName("distance")
    private double distance;

    public RecordRequest(int challengeId, double totalTime, double distance) {
        this.challengeId = challengeId;
        this.totalTime = totalTime;
        this.distance = distance;
    }
}
