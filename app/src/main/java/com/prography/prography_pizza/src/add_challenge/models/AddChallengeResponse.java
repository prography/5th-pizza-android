package com.prography.prography_pizza.src.add_challenge.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AddChallengeResponse {
    @SerializedName("data")
    private ArrayList<Data> data;

    public static class Data implements Serializable {
        @SerializedName("ChallengeId")
        private int challengeId;
        @SerializedName("UserId")
        private int userId;
        @SerializedName("routine_type")
        private String routineType;
        @SerializedName("quota")
        private double time;
        @SerializedName("object_unit")
        private String objectUnit;
        @SerializedName("exercise_type")
        private String exerciseType;
        @SerializedName("createdAt")
        private String createdAt;
        @SerializedName("updatedAt")
        private String updatedAt;

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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setRoutineType(String routineType) {
            this.routineType = routineType;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public void setObjectUnit(String objectUnit) {
            this.objectUnit = objectUnit;
        }

        public void setExerciseType(String exerciseType) {
            this.exerciseType = exerciseType;
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

        public Data(int challengeId, String userId, String routineType, double time, String objectUnit, String exerciseType, String createdAt, String updatedAt) {
            this.challengeId = challengeId;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
