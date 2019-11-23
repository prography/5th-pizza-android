package com.prography.progrpahy_pizza.src.addChallenge.interfaces;

import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeRequest;
import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddChallengeRetrofitInterface {
    @POST("addChallenge")
    Call<AddChallengeResponse> postRecord(@Body AddChallengeRequest recordRequest);

}
