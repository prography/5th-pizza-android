package com.prography.prography_pizza.src.addChallenge.interfaces;

import com.prography.prography_pizza.src.addChallenge.models.AddChallengeRequest;
import com.prography.prography_pizza.src.addChallenge.models.AddChallengeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddChallengeRetrofitInterface {
    @POST("challenges")
    Call<AddChallengeResponse> postChallenge(@Body AddChallengeRequest addChallengeRequest);

}
