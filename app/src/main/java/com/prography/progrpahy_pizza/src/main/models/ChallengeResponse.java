package com.prography.progrpahy_pizza.src.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChallengeResponse {
    @SerializedName("data") private ArrayList<Data> data;

    public static class Data {
        @SerializedName("id")
        private int challengeId;
        @SerializedName("routine_type")
        private String routineType;
        @SerializedName("quota")
        private double time;
        @SerializedName("object_unit")
        private String objectUnit;
        @SerializedName("exercise_type")
        private String exerciseType;
        @SerializedName("created_at")
        private String createdAt;

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

        public Data(int challengeId, String routineType, double time, String objectUnit, String exerciseType, String createdAt) {
            this.challengeId = challengeId;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
