package com.prography.prography_pizza.src.main;

import android.util.Log;

import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.interfaces.MainRetrofitInterface;
import com.prography.prography_pizza.src.main.models.MainDeleteResponse;
import com.prography.prography_pizza.src.main.models.MainResponse;

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
        mainRetrofitInterface.getChallenges().enqueue(new Callback<MainResponse>() {

            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                final MainResponse mainResponse = response.body();
                if (mainResponse == null) {
                    mMainActivityView.validateFailure();
                    return;
                }

                mMainActivityView.validateSuccess(mainResponse.getData());
            }


            public void onFailure(Call<MainResponse> call, Throwable t) {
                mMainActivityView.validateFailure();
            }
        });
    }

    public void deleteChallenge(final int challengeId) {
        final MainRetrofitInterface mainRetrofitInterface = getRetrofit().create(MainRetrofitInterface.class);
        mainRetrofitInterface.deleteChallenges(challengeId).enqueue(new Callback<MainDeleteResponse>() {
            @Override
            public void onResponse(Call<MainDeleteResponse> call, Response<MainDeleteResponse> response) {
                MainDeleteResponse mainDeleteResponse = response.body();
                if (mainDeleteResponse == null) {
                    mMainActivityView.validateFailure();
                    return;
                }
                mMainActivityView.validateDeleteSuccess(challengeId);
                Log.i("MAIN SERVICE", "DELETED");
            }

            @Override
            public void onFailure(Call<MainDeleteResponse> call, Throwable t) {
                t.printStackTrace();
                mMainActivityView.validateFailure();
            }
        });
    }
}
