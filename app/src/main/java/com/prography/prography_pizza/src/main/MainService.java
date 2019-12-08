package com.prography.prography_pizza.src.main;

import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.interfaces.MainRetrofitInterface;
import com.prography.prography_pizza.src.main.models.ChallengeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class MainService {
    final MainActivityView mMainActivityView;

    public MainService(final MainActivityView mainActivityView) {
        this.mMainActivityView = mainActivityView;
    }

    public void getChallege() {
        final MainRetrofitInterface mainRetrofitInterface = getRetrofit().create(MainRetrofitInterface.class);
        mainRetrofitInterface.getChallenges().enqueue(new Callback<ChallengeResponse>() {

            public void onResponse(Call<ChallengeResponse> call, Response<ChallengeResponse> response) {
                final ChallengeResponse challengeResponse = response.body();
                if (challengeResponse == null) {
                    mMainActivityView.validateFailure();
                    return;
                }

                mMainActivityView.validateSuccess(challengeResponse.getData());
            }


            public void onFailure(Call<ChallengeResponse> call, Throwable t) {
                mMainActivityView.validateFailure();
            }
        });
    }

    public void deleteChallenge(final int challengeId) {
        final MainRetrofitInterface mainRetrofitInterface = getRetrofit().create(MainRetrofitInterface.class);
        mainRetrofitInterface.deleteChallenges(challengeId).enqueue(new Callback<ChallengeResponse>() {
            @Override
            public void onResponse(Call<ChallengeResponse> call, Response<ChallengeResponse> response) {
                ChallengeResponse challengeResponse = response.body();
                if (challengeResponse == null) {
                    mMainActivityView.validateFailure();
                    return;
                }
                mMainActivityView.validateDeleteSuccess(challengeId);
            }

            @Override
            public void onFailure(Call<ChallengeResponse> call, Throwable t) {
                t.printStackTrace();
                mMainActivityView.validateFailure();
            }
        });
    }
}
