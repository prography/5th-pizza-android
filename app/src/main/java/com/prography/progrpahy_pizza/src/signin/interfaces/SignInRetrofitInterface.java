package com.prography.progrpahy_pizza.src.signin.interfaces;

import com.prography.progrpahy_pizza.src.signin.models.SignInParams;
import com.prography.progrpahy_pizza.src.signin.models.SignInResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInRetrofitInterface {

    @POST("auth/login")
    Call<SignInResponse> postTokenFromKakao(@Body SignInParams signInParams);
}
