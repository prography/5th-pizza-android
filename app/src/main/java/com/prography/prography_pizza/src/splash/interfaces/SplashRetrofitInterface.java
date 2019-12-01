package com.prography.prography_pizza.src.splash.interfaces;

import com.prography.prography_pizza.src.splash.models.SplashParams;
import com.prography.prography_pizza.src.splash.models.SplashResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SplashRetrofitInterface {
    @POST("auth/login")
    Call<SplashResponse> trySignIn(@Body SplashParams params);
}
