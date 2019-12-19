package com.prography.prography_pizza.src.splash.models;

import com.google.gson.annotations.SerializedName;

public class SplashResponse {
    public static class Data {
        @SerializedName("id")
        private int userUniqueId;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("email")
        private String userEmail;
        @SerializedName("nickname")
        private String userNickname;
        @SerializedName("created_at")
        private String createdAt;

        public int getUserUniqueId() {
            return userUniqueId;
        }

        public int getUserId() {
            return userId;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getUserNickname() {
            return userNickname;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    @SerializedName("data")
    private Data data;

    @SerializedName("access_token")
    private String accessToken;

    public Data getData() {
        return data;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
