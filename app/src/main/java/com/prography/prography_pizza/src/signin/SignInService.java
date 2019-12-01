package com.prography.prography_pizza.src.signin;

import com.prography.prography_pizza.src.signin.interfaces.SignInActivityView;
import com.prography.prography_pizza.src.signin.interfaces.SignInRetrofitInterface;
import com.prography.prography_pizza.src.signin.models.SignInParams;
import com.prography.prography_pizza.src.signin.models.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class SignInService {

    final SignInActivityView mSignInActivityView;

    public SignInService(SignInActivityView mSignInActivityView) {
        this.mSignInActivityView = mSignInActivityView;
    }

    public void getKakaoSignIn(String token) {
        final SignInRetrofitInterface signInRetrofitInterface = getRetrofit().create(SignInRetrofitInterface.class);
        signInRetrofitInterface.postTokenFromKakao(new SignInParams(token)).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                SignInResponse signInResponse = response.body();
                if (signInResponse == null) {
                    mSignInActivityView.validateKakaoFailure();
                }

                mSignInActivityView.validateKakaoSuccess(signInResponse.getAccessToken());
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                t.printStackTrace();
                mSignInActivityView.validateKakaoFailure();
            }
        });
    }
}
