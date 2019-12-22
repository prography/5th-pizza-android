package com.prography.prography_pizza.src.signin;

import com.prography.prography_pizza.config.XSocialTokenInterceptor;
import com.prography.prography_pizza.src.signin.interfaces.SignInActivityView;
import com.prography.prography_pizza.src.signin.interfaces.SignInRetrofitInterface;
import com.prography.prography_pizza.src.signin.models.SignInResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.prography.prography_pizza.src.ApplicationClass.BASE_URL;

public class SignInService {

    final SignInActivityView mSignInActivityView;

    public SignInService(SignInActivityView mSignInActivityView) {
        this.mSignInActivityView = mSignInActivityView;
    }

    public void getSignIn(String token, String type) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new XSocialTokenInterceptor(token)) // JWT 자동 헤더 전송
                .build();

        final SignInRetrofitInterface signInRetrofitInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SignInRetrofitInterface.class);

        signInRetrofitInterface.postSocialToken(type).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                SignInResponse signInResponse = response.body();
                if (signInResponse == null) {
                    mSignInActivityView.validateFailure();
                }

                assert signInResponse != null;
                mSignInActivityView.validateSuccess(signInResponse.getAccessToken());
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                t.printStackTrace();
                mSignInActivityView.validateFailure();
            }
        });
    }
}
