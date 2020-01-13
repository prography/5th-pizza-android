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
        @SerializedName("user_id")
        private int userId;
        @SerializedName("challenge_id")
        private int challengId;
        @SerializedName("running_time")
        private double runningTime;
        @SerializedName("distance")
        private double totalDistance;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("screenshot")
        private String recordImgUrl;

        public int getRecordId() {
            return recordId;
        }

        public int getUserId() {
            return userId;
        }

        public int getChallengId() {
            return challengId;
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

        public String getRecordImgUrl() {
            return recordImgUrl;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }


    public ChallengeDetailResponse(ArrayList<Data> data) {
        this.data = data;
    }
}
