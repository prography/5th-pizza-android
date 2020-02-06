package com.prography.prography_pizza.src.mypage.fragments.summary.interfaces;

import com.prography.prography_pizza.src.mypage.fragments.summary.models.BadgeResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SummaryRetrofitInterface {
    @GET("/badges")
    Call<BadgeResponse> getBadges();
}
