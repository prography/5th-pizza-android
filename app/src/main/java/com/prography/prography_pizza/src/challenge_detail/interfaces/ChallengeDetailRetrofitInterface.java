package com.prography.prography_pizza.src.challenge_detail.interfaces;

import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;
import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChallengeDetailRetrofitInterface {
    @GET("challenges/{challengeId}/records")
    Call<ChallengeDetailResponse> getChallengeDetail(@Path("challengeId") int challengeId);

    @GET("rank/challenge/{challengeId}")
    Call<RankResponse> getRanks(@Path("challengeId") int challengeId);
}
