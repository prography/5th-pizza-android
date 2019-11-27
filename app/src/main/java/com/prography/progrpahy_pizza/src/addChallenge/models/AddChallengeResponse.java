package com.prography.progrpahy_pizza.src.addChallenge.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddChallengeResponse {
    @SerializedName("data") private ArrayList<Data> data;

    public static class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("user_id") UserInfo userInfo;
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

        public int getId() {
            return id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public Data(int id, String routineType, double time, String objectUnit, String exerciseType, String createdAt) {
            this.id = id;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
        }

        public static class UserInfo {
            @SerializedName("id") private int userUniqueId;
            @SerializedName("user_id") private int userId;
            @SerializedName("email") private String userEmail;
            @SerializedName("nickname") private String userNickname;
            @SerializedName("created_at") private String createdAt;

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

        public UserInfo getUserInfo() {
            return userInfo;
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
