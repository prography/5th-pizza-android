package com.prography.prography_pizza.src.signin.interfaces;

import com.prography.prography_pizza.src.signin.models.SignInResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SignInRetrofitInterface {

    @POST("auth/login/{type}")
    Call<SignInResponse> postSocialToken(@Path("type") String socialType);
}
