package com.prography.prography_pizza.src.add_challenge.models;

import com.google.gson.annotations.SerializedName;

public class AddChallengeParams {
    @SerializedName("routine_type")
    private String routineType;
    @SerializedName("quota")
    private double time;
    @SerializedName("object_unit")
    private String objectUnit;
    @SerializedName("exercise_type")
    private String exerciseType;

    public AddChallengeParams(String routineType, double time, String objectUnit, String exerciseType) {
        this.routineType = routineType;
        this.time = time;
        this.objectUnit = objectUnit;
        this.exerciseType = exerciseType;
    }
}
