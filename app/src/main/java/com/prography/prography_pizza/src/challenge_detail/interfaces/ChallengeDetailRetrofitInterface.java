package com.prography.prography_pizza.src.challenge_detail.interfaces;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChallengeDetailRetrofitInterface {
    @GET("challenges/{:challengeId}")
    void getChallengeDetail(@Path("challengeId") int challengeId);
}
