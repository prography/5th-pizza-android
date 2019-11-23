package com.prography.progrpahy_pizza.src.record.interfaces;

import com.prography.progrpahy_pizza.src.record.models.RecordRequest;
import com.prography.progrpahy_pizza.src.record.models.RecordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RecordRetrofitInterface {
    @POST("records")
    Call<RecordResponse> postRecord(@Body RecordRequest recordRequest);
}
