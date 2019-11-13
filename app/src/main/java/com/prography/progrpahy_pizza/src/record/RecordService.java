package com.prography.progrpahy_pizza.src.record;

import com.prography.progrpahy_pizza.src.record.interfaces.RecordActivityView;
import com.prography.progrpahy_pizza.src.record.interfaces.RecordRetrofitInterface;
import com.prography.progrpahy_pizza.src.record.models.RecordRequest;
import com.prography.progrpahy_pizza.src.record.models.RecordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.progrpahy_pizza.src.ApplicationClass.getRetrofit;

public class RecordService {
    final RecordActivityView mRecordActivityView;

    public RecordService(RecordActivityView mRecordActivityView) {
        this.mRecordActivityView = mRecordActivityView;
    }

    public void postRecord() {
        final RecordRetrofitInterface recordRetrofitInterface = getRetrofit().create(RecordRetrofitInterface.class);
        recordRetrofitInterface.postRecord(new RecordRequest()).enqueue(new Callback<RecordResponse>() {
            @Override
            public void onResponse(Call<RecordResponse> call, Response<RecordResponse> response) {
                final RecordResponse recordResponse = response.body();
                if (recordResponse == null || !recordResponse.getIsSuccess()) {
                    return;
                    // Fail
                }
                // Success
            }

            @Override
            public void onFailure(Call<RecordResponse> call, Throwable t) {
                t.printStackTrace();
                // Fail
            }
        });
    }
}
