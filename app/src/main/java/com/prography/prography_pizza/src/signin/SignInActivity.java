package com.prography.prography_pizza.src.signin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.interfaces.SignInActivityView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.prography.prography_pizza.src.ApplicationClass.LOGIN_TYPE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_FACEBOOK;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_GOOGLE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_KAKAO;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.USER_EMAIL;
import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SignInActivity extends BaseActivity implements SignInActivityView {

    public static final int REQUEST_GOOGLE = 0;

    private ImageView btnKakaoImpl;
    private LoginButton btnKakao;
    private SignInButton btnGoogle;
    private ImageView btnGoogleImpl;
    private com.facebook.login.widget.LoginButton btnFacebook;
    private ImageView btnFacebookImpl;
    private OAuthLoginButton btnNaver;
    private ImageView btnNaverImpl;

    private SessionCallback mKakaoCallback;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mFacebookCallback;
    public OAuthLogin mOAuthLoginModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /* findViewById */
        btnKakao = findViewById(R.id.btn_kakao_signin_signin);
        btnKakaoImpl = findViewById(R.id.btn_kakao_signin_impl_signin);
        btnGoogle = findViewById(R.id.btn_google_signin_signin);
        btnGoogleImpl = findViewById(R.id.btn_google_signin_impl_signin);
        btnFacebook = findViewById(R.id.btn_facebook_signin_signin);
        btnFacebookImpl = findViewById(R.id.btn_facebook_signin_impl_signin);
        btnNaver = findViewById(R.id.btn_naver_signin_signin);
        btnNaverImpl = findViewById(R.id.btn_naver_signin_impl_signin);

        /* Kakao Session */
        mKakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mKakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        /* Google Session */
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_client_key))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        /* Facebook Session */
        mFacebookCallback = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions(Arrays.asList("email"));
        btnFacebook.registerCallback(mFacebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            sSharedPreferences.edit().putString(USER_NAME, object.getString("name"))
                                    .putString(USER_EMAIL, object.getString("email"))
                                    .putString(USER_PROFILE , object.getJSONObject("picture").getJSONObject("data").getString("url"))
                                    .putString(LOGIN_TYPE, TYPE_FACEBOOK)
                                    .apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String token = accessToken.getToken();
                        Log.i("FACEBOOK TOKEN", token);
                        tryGetToken(token, TYPE_FACEBOOK);

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        /* Naver Session */
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(this,getString(R.string.naver_client_id),getString(R.string.naver_client_key), getString(R.string.app_name));

        /* Set OnClick Listener */
        btnKakaoImpl.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnGoogleImpl.setOnClickListener(this);
        btnFacebookImpl.setOnClickListener(this);
        btnNaver.setOAuthLoginHandler(mOAuthLoginHandler);
        btnNaverImpl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kakao_signin_impl_signin:
                btnKakao.performClick();
                break;
            case R.id.btn_google_signin_impl_signin:
            case R.id.btn_google_signin_signin:
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_GOOGLE);
                break;
            case R.id.btn_facebook_signin_impl_signin:
                btnFacebook.performClick();
                break;
            case R.id.btn_naver_signin_impl_signin:
                btnNaver.performClick();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {

            return;
        }
        mFacebookCallback.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKakaoCallback);
    }

    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                final String accessToken = mOAuthLoginModule.getAccessToken(getApplicationContext());
                final String refreshToken = mOAuthLoginModule.getRefreshToken(getParent());

                Log.i("NAVER TOKEN", accessToken);

                Thread getProfileThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(mOAuthLoginModule.requestApi(getApplicationContext(), accessToken, "https://openapi.naver.com/v1/nid/me"))
                                    .getJSONObject("response");
                            sSharedPreferences.edit().putString(USER_PROFILE, jsonObject.getString("profile_image"))
                                    .putString(USER_EMAIL, jsonObject.getString("email"))
                                    .putString(USER_NAME, jsonObject.getString("name"))
                                    .putString(LOGIN_TYPE, "naver")
                                    .apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                getProfileThread.start();
                try {
                    getProfileThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryGetToken(accessToken, "naver");

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(getParent()).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(getParent());
                showToast("errorCode:" + errorCode + ", errorDesc:" + errorDesc);
            }
        };
    };

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                sSharedPreferences.edit().putString(USER_EMAIL, account.getEmail())
                        .putString(USER_NAME, account.getDisplayName())
                        .putString(USER_PROFILE, account.getPhotoUrl().toString())
                        .putString(LOGIN_TYPE, TYPE_GOOGLE)
                        .apply();

                String token = account.getIdToken();
                Log.i("GOOGLE TOKEN", token);

                tryGetToken(token, TYPE_GOOGLE);
            }
        } catch (ApiException e) {
            Log.w("GOOGLE FAIL", "handleGoogleSignInResult: " + e.getStatusCode());
            e.printStackTrace();
        }
    }

    @Override
    public void validateSuccess(String token) {
        hideProgressDialog();
        showToast("Success");
        sSharedPreferences.edit().putString(X_ACCESS_TOKEN, token).apply();
        startNextActivity(MainActivity.class);
        finish();
    }

    @Override
    public void validateFailure() {
        hideProgressDialog();
        showToast("Failure");
    }

    public void tryGetToken(String token, String type) {
        showProgressDialog();
        final SignInService signInService = new SignInService(this);
        signInService.getSignIn(token, type);
    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("kakao_account.profile");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                sSharedPreferences.edit().putString(USER_PROFILE, response.getKakaoAccount().getProfile().getProfileImageUrl())
                        .putString(USER_NAME, response.getKakaoAccount().getProfile().getNickname())
                        .putString(USER_EMAIL, response.getKakaoAccount().getEmail())
                        .putString(LOGIN_TYPE, TYPE_KAKAO)
                        .apply();
                String token = Session.getCurrentSession().getAccessToken();
                Log.i("KAKAO TOKEN", token);
                tryGetToken(token, TYPE_KAKAO);
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {

            }
        }
    }
}
