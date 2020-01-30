package com.prography.prography_pizza.src.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.common.utils.CustomPosNegDialog;
import com.prography.prography_pizza.src.common.utils.CustomSimpleMessageDialog;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.mypage.MyPageActivity;
import com.prography.prography_pizza.src.settings.interfaces.SettingActivityView;

import static com.prography.prography_pizza.src.ApplicationClass.LOGIN_TYPE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_FACEBOOK;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_GOOGLE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_KAKAO;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_NAVER;
import static com.prography.prography_pizza.src.ApplicationClass.USER_EMAIL;
import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SettingActivity extends BaseActivity implements SettingActivityView {


    private static Activity sActivity;

    private ImageView ivProfile;
    private ImageView ivChangeProfile;
    private TextView tvUserName;
    private TextView tvSignOut;
    private TextView tvResign;
    private TextView tvNickName;
    private TextView tvGender;
    private TextView tvEmail;

    private Toolbar tbSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* findViewByID */
        ivProfile = findViewById(R.id.iv_profile_settings);
        ivChangeProfile = findViewById(R.id.iv_change_profile_settings);
        tvUserName = findViewById(R.id.tv_username_settings);
        tbSettings = findViewById(R.id.toolbar_settings);
        tvSignOut = findViewById(R.id.tv_signout_settings);
        tvResign = findViewById(R.id.tv_resign_settings);
        tvNickName = findViewById(R.id.tv_nickname_desc_settings);
        tvGender = findViewById(R.id.tv_gender_desc_settings);
        tvEmail = findViewById(R.id.tv_email_settings);

        sActivity = this;


        /* Toolbar */
        setSupportActionBar(tbSettings);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* Set View */
        tvUserName.setText(sSharedPreferences.getString(USER_NAME, "Unknown") + "님");
        tvNickName.setText(sSharedPreferences.getString(USER_NAME, "Unknown") + "님");
        tvEmail.setText(sSharedPreferences.getString(USER_EMAIL, "이메일이 없습니다."));
        tvGender.setText("--");
        ivProfile.setClipToOutline(true);
        Glide.with(this)
                .load(sSharedPreferences.getString(USER_PROFILE, ""))
                .placeholder(R.drawable.kakao_default_profile_image)
                .error(R.drawable.kakao_default_profile_image)
                .circleCrop()
                .into(ivProfile);

        /* Set On Click Listener */
        ivChangeProfile.setOnClickListener(this);
        tvSignOut.setOnClickListener(this);
        tvResign.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_change_profile_settings:
                showSimpleMessageDialog(getString(R.string.not_implemented));
                break;
            case R.id.tv_signout_settings:
                // 임시 로그아웃
                showPosNegDialog("앱을 종료 후\n로그아웃하시겠습니까?", v1 -> {
                    // 앱 내 정보 삭제
                    String logInType = sSharedPreferences.getString(LOGIN_TYPE, TYPE_KAKAO);
                    sSharedPreferences.edit().remove(LOGIN_TYPE).remove(USER_EMAIL).remove(USER_NAME).remove(USER_PROFILE).apply();

                    final Activity mainActivity = MainActivity.sMainActivity;
                    final Activity myPageActivity = MyPageActivity.sMyPageActivity;

                    switch (logInType) {
                        case TYPE_GOOGLE:
                            // Google 로그아웃
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(),
                                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                            .requestEmail()
                                            .requestIdToken(getString(R.string.google_client_key))
                                            .build());
                            googleSignInClient.signOut().addOnCompleteListener(sActivity, task -> {
                                mainActivity.finish();
                                myPageActivity.finish();
                                finish();
                            });
                            break;
                        case TYPE_KAKAO:
                            // Kakao 로그아웃
                            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                    mainActivity.finish();
                                    myPageActivity.finish();
                                    finish();
                                }
                            });
                            break;
                        case TYPE_FACEBOOK:
                            // Facebook 로그아웃
                            LoginManager.getInstance().logOut();
                            mainActivity.finish();
                            myPageActivity.finish();
                            finish();
                            break;
                        case TYPE_NAVER:
                            // Naver 로그아웃
                            OAuthLogin mOauthLoginModule = OAuthLogin.getInstance();
                            mOauthLoginModule.init(getApplicationContext(), getString(R.string.naver_client_id), getString(R.string.naver_client_key), getString(R.string.app_name));
                            mOauthLoginModule.logoutAndDeleteToken(getApplicationContext());
                            mainActivity.finish();
                            myPageActivity.finish();
                            finish();
                            break;
                    }
                });
                break;
            case R.id.tv_resign_settings:
                showPosNegDialog("정말 탈퇴하시겠습니까?", v12 -> showSimpleMessageDialog("서버 점검중입니다.\n 잠시 후 다시 시도해 주세요.", getString(R.string.tv_confirm), CustomSimpleMessageDialog.FINISH_NONE, null));
                break;
        }
    }
}
