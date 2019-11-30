package com.prography.progrpahy_pizza.src.splash;

import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.util.exception.KakaoException;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;
import com.prography.progrpahy_pizza.src.signin.SignInActivity;
import com.prography.progrpahy_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.progrpahy_pizza.src.splash.models.SplashResponse;

import java.util.Date;

import static com.prography.progrpahy_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.progrpahy_pizza.src.ApplicationClass.sSharedPreferences;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        if (Session.getCurrentSession().checkAndImplicitOpen()) {

        } else {
            startNextActivity(SignInActivity.class);
            overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
            finish();
        }
    }


    @Override
    public void validateSuccess(SplashResponse.Data data, String token) {
        hideProgressDialog();
        sSharedPreferences.edit().putString(X_ACCESS_TOKEN, token).apply();
        startNextActivity(MainActivity.class);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        finish();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();

    }

    public void trySignIn(String kakaoToken) {
        final SplashService splashService = new SplashService(this);
        splashService.trySignIn(kakaoToken);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String token = Session.getCurrentSession().getAccessToken();
            if (token != null && !token.equals("")) {
                trySignIn(token);
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                exception.printStackTrace();

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
}
