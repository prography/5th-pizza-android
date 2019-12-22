package com.prography.prography_pizza.src.main.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;

import java.util.ArrayList;

public class MainResponse {
    @SerializedName("data")
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    @Entity
    public static class Data implements Parcelable {
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
        @PrimaryKey
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
        @SerializedName("achievement")
        private int achievement;
        @SerializedName("challengersNumber")
        private int challengersConut;

        protected Data(Parcel in) {
            challengeId = in.readInt();
            routineType = in.readString();
            objectUnit = in.readString();
            time = in.readDouble();
            exerciseType = in.readString();
            createdAt = in.readString();
            achievement = in.readInt();
            challengersConut = in.readInt();
        }

        public Data(int challengeId, String routineType, double time, String objectUnit, String exerciseType, String createdAt, int achievement, int challengersConut) {
            this.challengeId = challengeId;
            this.routineType = routineType;
            this.time = time;
            this.objectUnit = objectUnit;
            this.exerciseType = exerciseType;
            this.createdAt = createdAt;
            this.achievement = achievement;
            this.challengersConut = challengersConut;
        }

        public Data(AddChallengeResponse.Data datum) {
            this.challengeId = datum.getChallengeId();
            this.createdAt = datum.getCreatedAt();
            this.exerciseType = datum.getExerciseType();
            this.routineType = datum.getRoutineType();
            this.objectUnit = datum.getObjectUnit();
            this.time = datum.getTime();
            this.achievement = 0;
            this.challengersConut = 0;
        }

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

        public int getAchievement() {
            return achievement;
        }

        public int getChallengersConut() {
            return challengersConut;
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
            dest.writeInt(achievement);
            dest.writeInt(challengersConut);
        }
    }
}
