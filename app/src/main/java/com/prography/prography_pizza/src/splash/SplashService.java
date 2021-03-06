package com.prography.prography_pizza.src.splash;

import com.prography.prography_pizza.config.XSocialTokenInterceptor;
import com.prography.prography_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.prography_pizza.src.splash.interfaces.SplashRetrofitInterface;
import com.prography.prography_pizza.src.splash.models.SplashResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.prography.prography_pizza.src.ApplicationClass.BASE_URL;

public class SplashService {

    final SplashActivityView mSplashActivityView;

    public SplashService(SplashActivityView mSplashActivityView) {
        this.mSplashActivityView = mSplashActivityView;
    }

    public void trySignIn(String token, String type) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new XSocialTokenInterceptor(token)) // JWT 자동 헤더 전송
                .build();

        final SplashRetrofitInterface splashRetrofitInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SplashRetrofitInterface.class);

        splashRetrofitInterface.trySignIn(type).enqueue(new Callback<SplashResponse>() {
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
