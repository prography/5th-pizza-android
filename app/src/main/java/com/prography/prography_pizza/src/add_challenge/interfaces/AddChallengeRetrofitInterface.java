package com.prography.prography_pizza.src.add_challenge.interfaces;

import com.prography.prography_pizza.src.add_challenge.models.AddChallengeParams;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AddChallengeRetrofitInterface {
    @POST("challenges")
    Call<AddChallengeResponse> postChallenge(@Body AddChallengeParams addChallengeParams);

}
