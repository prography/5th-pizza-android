package com.prography.prography_pizza.src.mypage.fragments.history.interfaces;

import com.prography.prography_pizza.src.mypage.fragments.history.models.HistoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HistoryRetrofitInterface {
    @GET("challenges")
    Call<HistoryResponse> getChallenges();
}
