package com.prography.prography_pizza.src.splash.models;

import com.google.gson.annotations.SerializedName;

public class SplashParams {

    public static final int TYPE_GOOGLE = 0;
    public static final int TYPE_KAKAO = 1;
    public static final int TYPE_FACEBOOK = 2;
    public static final int TYPE_NAVER = 3;

    @SerializedName("x_kakao_token")
    private String kakaoToken = null;

    @SerializedName("x_google_token")
    private String googleToken = null;

    @SerializedName("x_facebook_token")
    private String facebookToken = null;

    @SerializedName("x_naver_token")
    private String naverToken = null;

    public SplashParams(String token, int type) {
        switch (type) {
            case TYPE_GOOGLE:
                googleToken = token;
                break;
            case TYPE_KAKAO:
                kakaoToken = token;
                break;
            case TYPE_FACEBOOK:
                facebookToken = token;
                break;
            case TYPE_NAVER:
                naverToken = token;
                break;
        }

    }
}
