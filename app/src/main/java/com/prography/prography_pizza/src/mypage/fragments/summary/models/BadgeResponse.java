package com.prography.prography_pizza.src.mypage.fragments.summary.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BadgeResponse {
    @SerializedName("data") private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public static class Data implements Parcelable {
        @SerializedName("id") private int id;
        @SerializedName("user_id") private String userId;
        @SerializedName("type") private String badgeType;
        @SerializedName("level") private int badgeLevel;

        protected Data(Parcel in) {
            id = in.readInt();
            userId = in.readString();
            badgeType = in.readString();
            badgeLevel = in.readInt();
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

        public int getId() {
            return id;
        }

        public String getUserId() {
            return userId;
        }

        public String getBadgeType() {
            return badgeType;
        }

        public int getBadgeLevel() {
            return badgeLevel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(userId);
            dest.writeString(badgeType);
            dest.writeInt(badgeLevel);
        }
    }
}
