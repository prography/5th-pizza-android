package com.prography.prography_pizza.src.challenge_detail.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.datatransport.runtime.time.WallTime;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RankResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Parcelable {
        @SerializedName("ranks")
        private ArrayList<Rank> ranks;

        protected Data(Parcel in) {
            ranks = in.createTypedArrayList(Rank.CREATOR);
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

        public ArrayList<Rank> getRanks() {
            return ranks;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(ranks);
        }

        public static class Rank implements Parcelable {
            @SerializedName("user") private User user;
            @SerializedName("achivement") private int acheivement;

            protected Rank(Parcel in) {
                user = in.readParcelable(User.class.getClassLoader());
                acheivement = in.readInt();
            }

            public static final Creator<Rank> CREATOR = new Creator<Rank>() {
                @Override
                public Rank createFromParcel(Parcel in) {
                    return new Rank(in);
                }

                @Override
                public Rank[] newArray(int size) {
                    return new Rank[size];
                }
            };

            public User getUser() {
                return user;
            }

            public int getAcheivement() {
                return acheivement;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(user, flags);
                dest.writeInt(acheivement);
            }

            public static class User implements Parcelable {
                @SerializedName("id")
                private int id;
                @SerializedName("userId")
                private String userId;
                @SerializedName("nickname")
                private String userName;
                @SerializedName("email")
                private String email;
                @SerializedName("type")
                private String loginType;
                @SerializedName("continuousRecord")
                private int ConsequentRecords;
                @SerializedName("createdAt") private String createdAt;
                @SerializedName("updatedAt") private String updatedAt;

                public int getId() {
                    return id;
                }

                public String getUserId() {
                    return userId;
                }

                public String getUserName() {
                    return userName;
                }

                public String getEmail() {
                    return email;
                }

                public String getLoginType() {
                    return loginType;
                }

                public int getConsequentRecords() {
                    return ConsequentRecords;
                }

                public String getCreatedAt() {
                    return createdAt;
                }

                public String getUpdatedAt() {
                    return updatedAt;
                }

                public static Creator<User> getCREATOR() {
                    return CREATOR;
                }

                protected User(Parcel in) {
                    id = in.readInt();
                    userId = in.readString();
                    userName = in.readString();
                    email = in.readString();
                    loginType = in.readString();
                    ConsequentRecords = in.readInt();
                    createdAt = in.readString();
                    updatedAt = in.readString();
                }

                public static final Creator<User> CREATOR = new Creator<User>() {
                    @Override
                    public User createFromParcel(Parcel in) {
                        return new User(in);
                    }

                    @Override
                    public User[] newArray(int size) {
                        return new User[size];
                    }
                };

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(id);
                    dest.writeString(userId);
                    dest.writeString(userName);
                    dest.writeString(email);
                    dest.writeString(loginType);
                    dest.writeInt(ConsequentRecords);
                    dest.writeString(createdAt);
                    dest.writeString(updatedAt);
                }
            }
        }
    }
}
