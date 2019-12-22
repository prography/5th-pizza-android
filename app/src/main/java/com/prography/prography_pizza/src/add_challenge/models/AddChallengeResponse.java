package com.prography.prography_pizza.src.add_challenge.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AddChallengeResponse {
    @SerializedName("data")
    private ArrayList<Data> data;

    public static class Data implements Parcelable {
        @SerializedName("id")
        private int challengeId;
        @SerializedName("routine_type")
        private String routineType;
        @SerializedName("object_unit")
        private String objectUnit;
        @SerializedName("quota")
        private double time;
        @SerializedName("exercise_type")
        private String exerciseType;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("UserChallenges")
        private UserChallenges userChallenges;

        protected Data(Parcel in) {
            challengeId = in.readInt();
            routineType = in.readString();
            objectUnit = in.readString();
            time = in.readDouble();
            exerciseType = in.readString();
            createdAt = in.readString();
            userChallenges = in.readParcelable(UserChallenges.class.getClassLoader());
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

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

        public UserChallenges getUserChallenges() {
            return userChallenges;
        }

        public Data(int challengeId, String routineType, double time, String objectUnit, String exerciseType, String createdAt) {
            this.challengeId = challengeId;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(challengeId);
            dest.writeString(routineType);
            dest.writeString(objectUnit);
            dest.writeDouble(time);
            dest.writeString(exerciseType);
            dest.writeString(createdAt);
            dest.writeParcelable(userChallenges, flags);
        }


        public static class UserChallenges implements Parcelable {
            @SerializedName("createdAt") private String createdAt;
            @SerializedName("updatedAt") private String updatedAt;
            @SerializedName("ChallengeId") private int challengeId;
            @SerializedName("UserId") private String userId;

            protected UserChallenges(Parcel in) {
                createdAt = in.readString();
                updatedAt = in.readString();
                challengeId = in.readInt();
                userId = in.readString();
            }

            public static final Creator<UserChallenges> CREATOR = new Creator<UserChallenges>() {
                @Override
                public UserChallenges createFromParcel(Parcel in) {
                    return new UserChallenges(in);
                }

                @Override
                public UserChallenges[] newArray(int size) {
                    return new UserChallenges[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(createdAt);
                dest.writeString(updatedAt);
                dest.writeInt(challengeId);
                dest.writeString(userId);
            }
        }
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
