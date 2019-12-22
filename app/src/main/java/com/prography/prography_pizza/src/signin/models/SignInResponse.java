package com.prography.prography_pizza.src.signin.models;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {

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

    public static class Data {
        @SerializedName("id")
        private int userUniqueId;
        @SerializedName("type")
        private String socialType;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("email")
        private String userEmail;
        @SerializedName("nickname")
        private String userNickname;

        public int getUserUniqueId() {
            return userUniqueId;
        }

        public String getUserId() {
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
}
