package com.prography.prography_pizza.src.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
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
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.interfaces.SignInActivityView;
import com.prography.prography_pizza.src.signin.models.SignInParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.prography.prography_pizza.src.ApplicationClass.LOGIN_TYPE;
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
    private ImageView btnNaverImpl;

    private SessionCallback mKakaoCallback;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mFacebookCallback;

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
        btnNaverImpl = findViewById(R.id.btn_naver_signin_impl_signin);

        /* Kakao Session */
        mKakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mKakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        /* Google Session */
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
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
                                    .putInt(LOGIN_TYPE, SignInParams.TYPE_FACEBOOK)
                                    .apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String token = accessToken.getToken();
                        tryGetToken(token, SignInParams.TYPE_FACEBOOK);

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

        /* Set OnClick Listener */
        btnKakaoImpl.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnGoogleImpl.setOnClickListener(this);
        btnFacebookImpl.setOnClickListener(this);
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
                showToast(getString(R.string.not_implemented));
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

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                sSharedPreferences.edit().putString(USER_EMAIL, account.getEmail())
                        .putString(USER_NAME, account.getDisplayName())
                        .putString(USER_PROFILE, account.getPhotoUrl().toString())
                        .putInt(LOGIN_TYPE, SignInParams.TYPE_GOOGLE)
                        .apply();

                Log.i("GOOGLE TOKEN", account.getIdToken());

                tryGetToken(account.getIdToken(), SignInParams.TYPE_GOOGLE);
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

    public void tryGetToken(String token, int type) {
        showProgressDialog();
        final SignInService signInService = new SignInService(this);
        signInService.getSignIn(token, type);
    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("kakao_account.profile");
        keys.add("kakao_account.email");

        Log.i("KAKAO TOKEN", "requestMe: ");
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
                        .putInt(LOGIN_TYPE, SignInParams.TYPE_KAKAO)
                        .apply();
                String token = Session.getCurrentSession().getAccessToken();
                Log.i("KAKAO TOKEN", token);
                tryGetToken(token, SignInParams.TYPE_KAKAO);
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
