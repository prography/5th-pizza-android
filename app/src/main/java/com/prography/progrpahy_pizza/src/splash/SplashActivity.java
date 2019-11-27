package com.prography.progrpahy_pizza.src.splash;

import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;
import com.prography.progrpahy_pizza.src.signin.SignInActivity;
import com.prography.progrpahy_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.progrpahy_pizza.src.splash.models.SplashResponse;

import static com.prography.progrpahy_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.progrpahy_pizza.src.ApplicationClass.sSharedPreferences;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }


    @Override
    public void validateSuccess(SplashResponse.Data data, String token) {
        hideProgressDialog();
        sSharedPreferences.edit().putString(X_ACCESS_TOKEN, token).apply();
        startNextActivity(MainActivity.class);
        finish();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        startNextActivity(SignInActivity.class);

        finish();
    }

    public void trySignIn(String kakaoToken) {
        final SplashService splashService = new SplashService(this);
        splashService.trySignIn(kakaoToken);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String token = Session.getCurrentSession().getAccessToken();
            trySignIn(token);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {

            }
        }
    }
}
