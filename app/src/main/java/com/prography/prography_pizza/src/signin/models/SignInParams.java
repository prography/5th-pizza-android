package com.prography.prography_pizza.src.signin.models;

import com.google.gson.annotations.SerializedName;
import com.prography.prography_pizza.src.splash.models.SplashParams;

public class SignInParams extends SplashParams {

    public SignInParams(String token, int type) {
        super(token, type);
    }
}
