package com.prography.prography_pizza.src.splash.models;

import com.google.gson.annotations.SerializedName;

public class SplashParams {
    @SerializedName("x_kakao_token") private String kakaoToken;

    public SplashParams(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }
}
