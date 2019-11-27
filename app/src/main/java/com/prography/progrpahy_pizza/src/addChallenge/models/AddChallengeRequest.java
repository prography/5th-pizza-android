package com.prography.progrpahy_pizza.src.addChallenge.models;

import com.google.gson.annotations.SerializedName;

public class AddChallengeRequest {
    @SerializedName("routine_type")private String routineType;
    @SerializedName("quota") private double time;
    @SerializedName("object_unit") private String objectUnit;
    @SerializedName("exercise_type")private String exerciseType;

    public AddChallengeRequest(String routineType, double time, String objectUnit, String exerciseType) {
        this.routineType = routineType;
        this.time = time;
        this.objectUnit = objectUnit;
        this.exerciseType = exerciseType;
    }
}
