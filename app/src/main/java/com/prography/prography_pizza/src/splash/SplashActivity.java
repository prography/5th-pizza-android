package com.prography.prography_pizza.src.splash;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.SignInActivity;
import com.prography.prography_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.prography_pizza.src.splash.models.SplashResponse;

import static com.prography.prography_pizza.src.ApplicationClass.LOGIN_TYPE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_FACEBOOK;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_GOOGLE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_KAKAO;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_NAVER;
import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    private SessionCallback callback;
    private GoogleSignInAccount googleSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String loginType = sSharedPreferences.getString(LOGIN_TYPE, TYPE_KAKAO);
        switch (loginType) {
            case TYPE_KAKAO:
                callback = new SessionCallback();
                Session.getCurrentSession().addCallback(callback);
                if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                    // Kakao session check
                    startNextActivity(SignInActivity.class);
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    finish();
                }
                break;
            case TYPE_GOOGLE:
                googleSession = GoogleSignIn.getLastSignedInAccount(this);
                if (googleSession != null) {
                    String googleToken = googleSession.getIdToken();
                    trySignIn(googleToken, TYPE_GOOGLE);
                }
                break;
            case TYPE_FACEBOOK:
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null && !accessToken.isExpired()) {
                    trySignIn(accessToken.getToken(), TYPE_FACEBOOK);
                }
                break;
            case TYPE_NAVER:
                OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(this, getString(R.string.naver_client_id), getString(R.string.naver_client_key), getString(R.string.app_name));
                mOAuthLoginModule.refreshAccessToken(this);
                String naverToken = mOAuthLoginModule.getAccessToken(this);
                trySignIn(naverToken, TYPE_NAVER);
                break;
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

    public void trySignIn(String token, String type) {
        final SplashService splashService = new SplashService(this);
        splashService.trySignIn(token, type);
        Log.i(type + "TOKEN ", token);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String token = Session.getCurrentSession().getAccessToken();
            if (token != null && !token.equals("")) {
                trySignIn(token, TYPE_KAKAO);
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
