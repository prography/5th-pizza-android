package com.prography.prography_pizza.src.splash;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.SignInActivity;
import com.prography.prography_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.prography_pizza.src.splash.models.SplashParams;
import com.prography.prography_pizza.src.splash.models.SplashResponse;

import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        GoogleSignInAccount googleSession = GoogleSignIn.getLastSignedInAccount(this);
        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            // Kakao session check
        } else if (googleSession != null) {
            // Google session check
            String googleToken = googleSession.getIdToken();

            Log.i("GOOGLE TOKEN", googleToken);
            trySignIn(googleToken, SplashParams.TYPE_GOOGLE);
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

    public void trySignIn(String token, int type) {
        final SplashService splashService = new SplashService(this);
        splashService.trySignIn(token, type);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String token = Session.getCurrentSession().getAccessToken();
            if (token != null && !token.equals("")) {
                trySignIn(token, SplashParams.TYPE_KAKAO);
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
