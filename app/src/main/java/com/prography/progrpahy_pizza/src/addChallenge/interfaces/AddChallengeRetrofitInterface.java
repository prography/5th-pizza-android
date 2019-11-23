package com.prography.progrpahy_pizza.src.addChallenge.interfaces;

import com.prography.progrpahy_pizza.src.addChallenge.models.ChallengeRequest;
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddChallengeRetrofitInterface {
    @POST("challenges")
    Call<ChallengeResponse> postChallenge(@Body ChallengeRequest challengeRequest);

}
