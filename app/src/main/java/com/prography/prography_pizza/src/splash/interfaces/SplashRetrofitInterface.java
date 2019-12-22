package com.prography.prography_pizza.src.splash.interfaces;

import com.prography.prography_pizza.src.splash.models.SplashResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SplashRetrofitInterface {
    @POST("auth/login/{type}")
    Call<SplashResponse> trySignIn(@Path("type") String socialType);
}
