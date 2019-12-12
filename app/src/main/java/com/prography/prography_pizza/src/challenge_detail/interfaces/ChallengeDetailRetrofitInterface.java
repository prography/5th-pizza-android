package com.prography.prography_pizza.src.challenge_detail.interfaces;

import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChallengeDetailRetrofitInterface {
    @GET("challenges/{:challengeId}")
    Call<ChallengeDetailResponse> getChallengeDetail(@Path("challengeId") int challengeId);
}
