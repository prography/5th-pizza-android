package com.prography.prography_pizza.src.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.signin.interfaces.SignInActivityView;
import com.prography.prography_pizza.src.signin.models.SignInParams;

import java.util.ArrayList;
import java.util.List;

import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.USER_EMAIL;
import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SignInActivity extends BaseActivity implements SignInActivityView {

    public static final int REQUEST_GOOGLE = 0;

    private Button btnKakaoImpl;
    private LoginButton btnKakao;
    private SignInButton btnGoogle;
    private com.facebook.login.widget.LoginButton btnFacebook;

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
        btnFacebook = findViewById(R.id.btn_facebook_signin_signin);

        /* Kakao Session */
        mKakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mKakaoCallback);
        Session.getCurrentSession().checkAndImplicitOpen();

        /* Google Session */
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        /* Facebook Session */
        mFacebookCallback = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(mFacebookCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                tryGetToken(token, SignInParams.TYPE_FACEBOOK);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kakao_signin_impl_signin:
                btnKakao.performClick();
                break;
            case R.id.btn_google_signin_signin:
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_GOOGLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {

            return;
        }
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
                        .apply();

                tryGetToken(account.getIdToken(), SignInParams.TYPE_GOOGLE);
            }
        } catch (ApiException e) {
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
                sSharedPreferences.edit().putString(USER_PROFILE, response.getKakaoAccount().getProfile().getProfileImageUrl()).apply();
                sSharedPreferences.edit().putString(USER_NAME, response.getKakaoAccount().getProfile().getNickname()).apply();
                sSharedPreferences.edit().putString(USER_EMAIL, response.getKakaoAccount().getEmail()).apply();
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
