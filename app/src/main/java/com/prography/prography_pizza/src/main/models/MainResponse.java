package com.prography.prography_pizza.src.main.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;

import java.util.ArrayList;

public class MainResponse {
    @SerializedName("data")
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public static class Data implements Parcelable {
        @SerializedName("id") private int challengeId;
        @SerializedName("user_id") private String userId;
        @SerializedName("base_challenge_id") private int baseChallengeId;
        @SerializedName("start") private String startDate;
        @SerializedName("end") private String endDate;
        @SerializedName("success") private boolean success;
        @SerializedName("achievement") private int achievement;
        @SerializedName("challengersNumber") private int challengersConut;
        @SerializedName("created_at") private String createdAt;
        @SerializedName("updated_at") private String updatedAt;
        @SerializedName("base_challenge") private BaseChallengeData baseChallengeData;

        public Data(int challengeId, String userId, String startDate, String endDate, boolean success, int achievement, String createdAt, String updatedAt, String routineType, String objectUnit, double time, String exerciseType, int challengersConut, BaseChallengeData baseChallengeData) {
            this.challengeId = challengeId;
            this.userId = userId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.success = success;
            this.achievement = achievement;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.baseChallengeData = new BaseChallengeData(routineType, objectUnit, time, exerciseType);
            this.challengersConut = challengersConut;
            this.baseChallengeData = baseChallengeData;
        }

        protected Data(Parcel in) {
            challengeId = in.readInt();
            userId = in.readString();
            baseChallengeId = in.readInt();
            startDate = in.readString();
            endDate = in.readString();
            success = in.readByte() != 0;
            achievement = in.readInt();
            challengersConut = in.readInt();
            createdAt = in.readString();
            updatedAt = in.readString();
            baseChallengeData = in.readParcelable(BaseChallengeData.class.getClassLoader());
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

        public String getUserId() {
            return userId;
        }

        public int getBaseChallengeId() {
            return baseChallengeId;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public BaseChallengeData getBaseChallengeData() {
            return baseChallengeData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(challengeId);
            dest.writeString(userId);
            dest.writeInt(baseChallengeId);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeByte((byte) (success ? 1 : 0));
            dest.writeInt(achievement);
            dest.writeInt(challengersConut);
            dest.writeString(createdAt);
            dest.writeString(updatedAt);
            dest.writeParcelable(baseChallengeData, flags);
        }


        public static class BaseChallengeData implements Parcelable{
            @SerializedName("routine_type") private String routineType;
            @SerializedName("object_unit") private String objectUnit;
            @SerializedName("quota") private double time;
            @SerializedName("exercise_type") private String exerciseType;

            protected BaseChallengeData(Parcel in) {
                routineType = in.readString();
                objectUnit = in.readString();
                time = in.readDouble();
                exerciseType = in.readString();
            }

            public static final Creator<BaseChallengeData> CREATOR = new Creator<BaseChallengeData>() {
                @Override
                public BaseChallengeData createFromParcel(Parcel in) {
                    return new BaseChallengeData(in);
                }

                @Override
                public BaseChallengeData[] newArray(int size) {
                    return new BaseChallengeData[size];
                }
            };

            public String getRoutineType() {
                return routineType;
            }

            public String getObjectUnit() {
                return objectUnit;
            }

            public double getTime() {
                return time;
            }

            public String getExerciseType() {
                return exerciseType;
            }

            public BaseChallengeData(String routineType, String objectUnit, double time, String exerciseType) {
                this.routineType = routineType;
                this.objectUnit = objectUnit;
                this.time = time;
                this.exerciseType = exerciseType;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(routineType);
                dest.writeString(objectUnit);
                dest.writeDouble(time);
                dest.writeString(exerciseType);
            }
        }
    }
}
