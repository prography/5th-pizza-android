package com.prography.progrpahy_pizza.src.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.signin.interfaces.SignInActivityView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SignInActivity extends BaseActivity implements SignInActivityView {

    private SessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
          String token = Session.getCurrentSession().getAccessToken();
            Log.i("KAKAO TOKEN", token);
            tryGetKakaoToken(token);
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
}
