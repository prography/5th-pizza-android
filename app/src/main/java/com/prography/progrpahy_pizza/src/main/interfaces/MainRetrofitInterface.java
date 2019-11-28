package com.prography.progrpahy_pizza.src.main.interfaces;

import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MainRetrofitInterface {

    @GET("challenges")
    Call<ChallengeResponse> getChallenges();

    @DELETE("challenges/{challengeId}")
    Call<ChallengeResponse> deleteChallenges(@Path("challengeId") int challengeId);

}
