package com.prography.progrpahy_pizza.src.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;
import com.prography.progrpahy_pizza.src.signin.interfaces.SignInActivityView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import static com.prography.progrpahy_pizza.src.ApplicationClass.KAKAO_PROFILE;
import static com.prography.progrpahy_pizza.src.ApplicationClass.KAKAO_USEREMAIL;
import static com.prography.progrpahy_pizza.src.ApplicationClass.KAKAO_USERNAME;
import static com.prography.progrpahy_pizza.src.ApplicationClass.sSharedPreferences;

public class SignInActivity extends BaseActivity implements SignInActivityView {

    private SessionCallback callback;
    private Button btnKakao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /*callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();*/


        btnKakao=findViewById(R.id.btn_kakaoLogin);
        btnKakao.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL,this);
        Session.getCurrentSession().checkAndImplicitOpen();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    @Override
    public void validateKakaoSuccess() {
        hideProgressDialog();
        showToast("Success");
        startNextActivity(MainActivity.class);
        finish();
    }

    @Override
    public void validateKakaoFailure() {
        hideProgressDialog();
        showToast("Failure");
    }

    public void tryGetKakaoToken(String token) {
        showProgressDialog();
        final SignInService signInService = new SignInService(this);
        signInService.getKakaoSignIn(token);
    }

    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("properties.nickname");
        keys.add("kakao_account.profile");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                /*String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);*/
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                sSharedPreferences.edit().putString(KAKAO_PROFILE, response.getKakaoAccount().getProfile().getProfileImageUrl()).apply();
                sSharedPreferences.edit().putString(KAKAO_USERNAME, response.getKakaoAccount().getDisplayId()).apply();
                sSharedPreferences.edit().putString(KAKAO_USEREMAIL, response.getKakaoAccount().getEmail()).apply();
                String token = Session.getCurrentSession().getAccessToken();
                Log.i("KAKAO TOKEN", token);
                tryGetKakaoToken(token);
            }
        });
    }
}
