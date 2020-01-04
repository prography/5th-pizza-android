package com.prography.prography_pizza.src.splash;

import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.SignInActivity;
import com.prography.prography_pizza.src.splash.interfaces.SplashActivityView;
import com.prography.prography_pizza.src.splash.models.SplashResponse;
import com.prography.prography_pizza.src.tutorial.TutorialActivity;

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

        // SHA1: FF:36:5E:DC:DF:B8:CB:A5:C6:2D:51:09:87:1C:4D:D7:2C:66:35:1D

        byte[] sha1 = {
                (byte)0xFF, (byte)0x36, (byte)0x5E, (byte)0xDC, (byte)0xDF, (byte)0xB8, (byte)0xCB, (byte)0xA5, (byte)0xC6, 0x2D, 0x51, (byte)0x09, (byte)0x87, (byte)0x1C, 0x4D, (byte)0xD7, (byte)0x2C, (byte)0x66, (byte)0x35, (byte)0x1D
        };
        Logger.e("keyHash: " + Base64.encodeToString(sha1, Base64.NO_WRAP));

        String loginType = sSharedPreferences.getString(LOGIN_TYPE, "");
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
                final OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(this, getString(R.string.naver_client_id), getString(R.string.naver_client_key), getString(R.string.app_name));
                final String[] naverToken = new String[1];
                Thread naverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        naverToken[0] = mOAuthLoginModule.refreshAccessToken(getApplicationContext());
                    }
                });
                naverThread.start();
                try {
                    naverThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                trySignIn(naverToken[0], TYPE_NAVER);
                break;
            default:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNextActivity(TutorialActivity.class);
                        finish();
                    }
                }, 1000);
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
        startNextActivity(SignInActivity.class);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        finish();
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
