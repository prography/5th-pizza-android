package com.prography.prography_pizza.src.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.nhn.android.naverlogin.OAuthLogin;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.db.ChallengeModel;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.add_challenge.AddChallengeActivity;
import com.prography.prography_pizza.src.common.utils.RecyclerViewDecoration;
import com.prography.prography_pizza.src.main.adapter.ChallengeListAdapter;
import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.mypage.MyPageActivity;

import java.util.ArrayList;

import static com.prography.prography_pizza.src.ApplicationClass.LOGIN_TYPE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_FACEBOOK;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_GOOGLE;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_KAKAO;
import static com.prography.prography_pizza.src.ApplicationClass.TYPE_NAVER;
import static com.prography.prography_pizza.src.ApplicationClass.USER_EMAIL;
import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int REQUEST_CODE = 1;
    private FloatingActionButton fbtnAddChallenge;
    private RecyclerView rvMain;
    private Toolbar tbMain;
    private ArrayList<MainResponse.Data> challengeResponseList;
    private SwipeRefreshLayout srlMain;
    private ChallengeListAdapter clAdapter;
    private AppBarLayout ablMain;
    private TextView tvTitle;
    private TextView tvTitleCollapsed;
    private ImageView ivProfile;
    private ImageView ivProfileNext;

    public static Activity sMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* findViewByID */
        fbtnAddChallenge = findViewById(R.id.fbtn_add_challenge_main);
        rvMain = findViewById(R.id.recyclerView);
        tbMain = findViewById(R.id.toolbar_main);
        srlMain = findViewById(R.id.srl_main);
        ablMain = findViewById(R.id.abl_main);
        tvTitle = findViewById(R.id.tv_title_expanded_main);
        tvTitleCollapsed = findViewById(R.id.tv_toolbar_title_collapsed_main);
        ivProfile = findViewById(R.id.iv_profile_expanded_main);
        ivProfileNext = findViewById(R.id.iv_next_profile_main);

        /* Set static Activity */
        sMainActivity = this;

        /* Get Contents From Server... */
        tryGetChallenge();

        /* AppbarLayout OffSet Change Listener */
        ablMain.addOnOffsetChangedListener(this);

        /* RecyclerView */
        challengeResponseList = new ArrayList<>();
        clAdapter = new ChallengeListAdapter(challengeResponseList, this, this);
        rvMain.setAdapter(clAdapter);
        rvMain.addItemDecoration(new RecyclerViewDecoration(30));

        /* Set On Click Listener */
        fbtnAddChallenge.setOnClickListener(this);
        ablMain.setOnClickListener(this);
        tbMain.setOnClickListener(this);
        ivProfileNext.setOnClickListener(this);
        ivProfile.setOnClickListener(this);

        /* Set On Refresh Listener */
        srlMain.setOnRefreshListener(this);

        /* Init View */
        ivProfile.setBackground(new ShapeDrawable(new OvalShape()));
        ivProfile.setClipToOutline(true);
        Glide.with(this)
                .load(sSharedPreferences.getString(USER_PROFILE, ""))
                .placeholder(R.drawable.kakao_default_profile_image)
                .error(R.drawable.kakao_default_profile_image)
                .centerCrop()
                .into(ivProfile);
    }

    @Override
    public void validateSuccess(ArrayList<MainResponse.Data> data) {
        hideProgressDialog();

        /* Set View */
        challengeResponseList = data;
        clAdapter.setItems(challengeResponseList);
        tvTitle.setText("오늘 도전할 챌린지가\n" + clAdapter.getItemCount() + "개 있습니다");
        tvTitleCollapsed.setText("오늘의 챌린지: " + clAdapter.getItemCount() + "개");

        /* Saving to Local DB... */
        ChallengeModel challengeModel = new ChallengeModel(this);
        challengeModel.insertData(data);
    }

    @Override
    public void validateFailure() {
        hideProgressDialog();
        showSimpleMessageDialog(getString(R.string.network_error));
    }

    @Override
    public void validateDeleteSuccess(int challengeId) {
        hideProgressDialog();
        showSimpleMessageDialog("챌린지를 삭제하였습니다.");

        /* Saving to Local DB... */
        ChallengeModel challengeModel = new ChallengeModel(this);
        challengeModel.delete(challengeId);

        /* Set View */
        tvTitle.setText("오늘 도전할 챌린지가\n" + clAdapter.getItemCount() + "개 있습니다");
        tvTitleCollapsed.setText("오늘의 챌린지: " + clAdapter.getItemCount() + "개");
    }

    @Override
    public void startDeleteProcess(int challengeId) {
        showProgressDialog();
        final MainService mainService = new MainService(this);
        mainService.deleteChallenge(challengeId);
    }

    private void tryGetChallenge() {
        showProgressDialog();
        MainService mainService = new MainService(this);
        mainService.getChallege();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbtn_add_challenge_main:
                Intent intent = new Intent(this, AddChallengeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                break;
            case R.id.abl_main:
            case R.id.toolbar_main:
                ablMain.setExpanded(true, true);
                break;
            case R.id.iv_next_profile_main:
            case R.id.iv_profile_expanded_main:
                startNextActivity(MyPageActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get Data From Server...
                tryGetChallenge();
            }
        }
    }

    @Override
    public void onRefresh() {
        tryGetChallenge();
        srlMain.setRefreshing(false);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        float collapsedTitleAlpha = 1.0f - Math.abs((i + appBarLayout.getTotalScrollRange()) * 2.5f / (float) appBarLayout.getTotalScrollRange());
        if (collapsedTitleAlpha <= 0.0f)
            collapsedTitleAlpha = 0.0f;
        else if (collapsedTitleAlpha >= 1.0f)
            collapsedTitleAlpha = 1.0f;
        float expandedTitleAlpha = Math.abs((i + appBarLayout.getTotalScrollRange()) * 2.5f / (float) appBarLayout.getTotalScrollRange()) - 1.0f;
        if (expandedTitleAlpha <= 0.0f)
            expandedTitleAlpha = 0.0f;
        else if (expandedTitleAlpha >= 1.0f)
            expandedTitleAlpha = 1.0f;
        tvTitle.setAlpha(expandedTitleAlpha);
        ivProfile.setAlpha(expandedTitleAlpha);
        ivProfileNext.setAlpha(expandedTitleAlpha);
        tvTitleCollapsed.setAlpha(collapsedTitleAlpha);
    }



}
