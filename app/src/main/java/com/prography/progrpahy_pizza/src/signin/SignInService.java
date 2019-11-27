package com.prography.progrpahy_pizza.src.signin;

import com.prography.progrpahy_pizza.src.signin.interfaces.SignInActivityView;
import com.prography.progrpahy_pizza.src.signin.interfaces.SignInRetrofitInterface;
import com.prography.progrpahy_pizza.src.signin.models.SignInParams;
import com.prography.progrpahy_pizza.src.signin.models.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.progrpahy_pizza.src.ApplicationClass.getRetrofit;

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
                if (response == null) {
                    mSignInActivityView.validateKakaoFailure();
                }

                mSignInActivityView.validateKakaoSuccess();
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                t.printStackTrace();
                mSignInActivityView.validateKakaoFailure();
            }
        });
    }
}
