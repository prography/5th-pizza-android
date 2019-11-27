package com.prography.progrpahy_pizza.src.signin.models;

import com.google.gson.annotations.SerializedName;

public class SignInParams {
    @SerializedName("x_kakao_token") private String x_kakao_token;

    public SignInParams(String x_kakao_access) {
        this.x_kakao_token = x_kakao_access;
    }
}
