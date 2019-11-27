package com.prography.progrpahy_pizza.src.splash;

import com.prography.progrpahy_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.progrpahy_pizza.src.splash.interfaces.SplashRetrofitInterface;
import com.prography.progrpahy_pizza.src.splash.models.SplashParams;
import com.prography.progrpahy_pizza.src.splash.models.SplashResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.progrpahy_pizza.src.ApplicationClass.getRetrofit;

public class SplashService {

    final SplashActivityView mSplashActivityView;

    public SplashService(SplashActivityView mSplashActivityView) {
        this.mSplashActivityView = mSplashActivityView;
    }

    public void trySignIn(String kakaoToken) {
        final SplashRetrofitInterface splashRetrofitInterface = getRetrofit().create(SplashRetrofitInterface.class);
        splashRetrofitInterface.trySignIn(new SplashParams(kakaoToken)).enqueue(new Callback<SplashResponse>() {
            @Override
            public void onResponse(Call<SplashResponse> call, Response<SplashResponse> response) {
                SplashResponse splashResponse = response.body();
                if (splashResponse == null || splashResponse.getData() == null) {
                    mSplashActivityView.validateFailure(null);
                    return;
                }

                mSplashActivityView.validateSuccess(splashResponse.getData(), splashResponse.getAccessToken());
            }

            @Override
            public void onFailure(Call<SplashResponse> call, Throwable t) {
                t.printStackTrace();
                mSplashActivityView.validateFailure(null);
            }
        });
    }
}
