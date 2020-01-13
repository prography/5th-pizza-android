package com.prography.prography_pizza.src.challenge_detail.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RankResponse {
    private ArrayList<Data> data;

    public static class Data implements Serializable {
        @SerializedName("user_id") private String userName;
        @SerializedName("user_profile") private String profileUrl;
        @SerializedName("rank") private int rank;
        @SerializedName("progress") private int progress;

        public String getUserName() {
            return userName;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public int getRank() {
            return rank;
        }

        public int getProgress() {
            return progress;
        }

        public Data(int rank, String userName, String profileUrl, int progress) {
            this.userName = userName;
            this.profileUrl = profileUrl;
            this.rank = rank;
            this.progress = progress;
        }
    }
}
