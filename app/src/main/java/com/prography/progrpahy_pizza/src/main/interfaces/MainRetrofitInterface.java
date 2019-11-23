package com.prography.progrpahy_pizza.src.main.interfaces;

import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MainRetrofitInterface {

    @GET("/challenges")
    Call<ChallengeResponse> getChallenges();

    @POST("/challenges")
    Call<ChallengeResponse> postChallenges();


}
