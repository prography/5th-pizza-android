package com.prography.prography_pizza.src.main.interfaces;

import com.prography.prography_pizza.src.main.models.MainDeleteResponse;
import com.prography.prography_pizza.src.main.models.MainResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MainRetrofitInterface {

    @GET("challenges")
    Call<MainResponse> getChallenges();

    @DELETE("challenges/{challengeId}")
    Call<MainDeleteResponse> deleteChallenges(@Path("challengeId") int challengeId);

}
