package com.prography.progrpahy_pizza.src.addChallenge.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AddChallengeResponse {
    @SerializedName("data") private Data datum;

    public static class Data implements Serializable {
        @SerializedName("id") private int challengeId;
        @SerializedName("userId") private int userId;
        @SerializedName("routine_type") private String routineType;
        @SerializedName("quota") private double time;
        @SerializedName("object_unit") private String objectUnit;
        @SerializedName("exercise_type") private String exerciseType;
        @SerializedName("created_at") private String createdAt;

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

        public int getChallengeId() {
            return challengeId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getUserId() {
            return userId;
        }

        public Data(int challengeId, String userId, String routineType, double time, String objectUnit, String exerciseType, String createdAt) {
            this.challengeId = challengeId;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
        }
    }

    public Data getDatum() {
        return datum;
    }
}
