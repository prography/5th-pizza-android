package com.prography.prography_pizza.src.mypage.fragments.history;

import com.prography.prography_pizza.src.mypage.fragments.history.interfaces.HistoryFragmentView;
import com.prography.prography_pizza.src.mypage.fragments.history.interfaces.HistoryRetrofitInterface;
import com.prography.prography_pizza.src.mypage.fragments.history.models.HistoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class HistoryService {
    final HistoryFragmentView mHistoryFragmentView;

    public HistoryService(HistoryFragmentView mHistoryFragmentView) {
        this.mHistoryFragmentView = mHistoryFragmentView;
    }

    public void getChallenges() {
        final HistoryRetrofitInterface historyRetrofitInterface = getRetrofit().create(HistoryRetrofitInterface.class);
        historyRetrofitInterface.getChallenges().enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                HistoryResponse historyResponse = response.body();
                if (historyResponse == null) {
                    mHistoryFragmentView.validateFailure(null);
                    return;
                }
                mHistoryFragmentView.validateSuccess(historyResponse.getData());
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                mHistoryFragmentView.validateFailure(null);
            }
        });
    }
}
