package com.prography.prography_pizza.src.challenge_detail.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ChallengeDetailResponse {
    @SerializedName("data")
    private ArrayList<Data> data;

    public static class Data implements Serializable {
        @SerializedName("id")
        private int recordId;
        @SerializedName("exercise_type")
        private String exerciseType;
        @SerializedName("running_time")
        private double runningTime;
        @SerializedName("distance")
        private double totalDistance;
        @SerializedName("created_at")
        private String createdAt;

        public int getRecordId() {
            return recordId;
        }

        public String getExerciseType() {
            return exerciseType;
        }

        public double getRunningTime() {
            return runningTime;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public Data(int recordId, String exerciseType, double runningTime, double totalDistance, String createdAt) {
            this.recordId = recordId;
            this.exerciseType = exerciseType;
            this.runningTime = runningTime;
            this.totalDistance = totalDistance;
            this.createdAt = createdAt;
        }
    }


    public ArrayList<Data> getData() {
        return data;
    }


    public ChallengeDetailResponse(ArrayList<Data> data) {
        this.data = data;
    }
}
