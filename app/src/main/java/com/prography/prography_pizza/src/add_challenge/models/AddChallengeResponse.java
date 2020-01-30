package com.prography.prography_pizza.src.add_challenge.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.prography.prography_pizza.src.main.models.MainResponse;

import java.util.ArrayList;

public class AddChallengeResponse {
    @SerializedName("data")
    private Data data;

    public static class Data implements Parcelable {
        @SerializedName("error") private String error = null; // set On if error response.

        @SerializedName("id") private int challengeId;
        @SerializedName("user_id") private String userId;
        @SerializedName("base_challenge_id") private int baseChallengeId;
        @SerializedName("start") private String startDate;
        @SerializedName("end") private String endDate;
        @SerializedName("success") private boolean success;
        @SerializedName("created_at") private String createdAt;
        @SerializedName("updated_at") private String updatedAt;
        @SerializedName("base_challenge") private BaseChallengeData baseChallengeData;

        protected Data(Parcel in) {
            error = in.readString();
            challengeId = in.readInt();
            userId = in.readString();
            baseChallengeId = in.readInt();
            startDate = in.readString();
            endDate = in.readString();
            success = in.readByte() != 0;
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

        public String getError() {
            return error;
        }

        public int getChallengeId() {
            return challengeId;
        }

        public String getCreatedAt() {
            return createdAt;
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

        public Data(String error, int challengeId, String userId, int baseChallengeId, String startDate, String endDate, boolean success, String createdAt, String updatedAt, BaseChallengeData baseChallengeData) {
            this.error = error;
            this.challengeId = challengeId;
            this.userId = userId;
            this.baseChallengeId = baseChallengeId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.success = success;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.baseChallengeData = baseChallengeData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(error);
            dest.writeInt(challengeId);
            dest.writeString(userId);
            dest.writeInt(baseChallengeId);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeByte((byte) (success ? 1 : 0));
            dest.writeString(createdAt);
            dest.writeString(updatedAt);
            dest.writeParcelable(baseChallengeData, flags);
        }

        public static class BaseChallengeData extends MainResponse.Data.BaseChallengeData {

            protected BaseChallengeData(Parcel in) {
                super(in);
            }

            public BaseChallengeData(String routineType, String objectUnit, double time, String exerciseType) {
                super(routineType, objectUnit, time, exerciseType);
            }
        }

    }

    public Data getData() {
        return data;
    }
}
