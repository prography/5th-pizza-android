package com.prography.prography_pizza.src.challenge_detail.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChallengeDetailResponse {
    @SerializedName("data") private ArrayList<Data> data;

    public static class Data implements Serializable{
        @SerializedName("id") private int challengeId;
        @SerializedName("routine_type") private String routineType;
        @SerializedName("quota") private double time;
        @SerializedName("object_unit") private String objectUnit;
        @SerializedName("exercise_type") private String exerciseType;
        @SerializedName("created_at") private String createdAt;

        public int getChallengeId() {
            return challengeId;
        }

        public String getRoutineType() {
            return routineType;
        }

        public double getTime() {
            return time;
        }

        public String getObjectUnit() {
            return objectUnit;
        }

        public String getExerciseType() {
            return exerciseType;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
