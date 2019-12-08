package com.prography.prography_pizza.src.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.db.ChallengeModel;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.add_challenge.AddChallengeActivity;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;
import com.prography.prography_pizza.src.common.utils.RecyclerViewDecoration;
import com.prography.prography_pizza.src.main.adapter.ChallengeListAdapter;
import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.models.ChallengeResponse;
import com.prography.prography_pizza.src.mypage.MyPageActivity;

import java.util.ArrayList;

import static com.prography.prography_pizza.src.ApplicationClass.KAKAO_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class MainActivity extends BaseActivity implements MainActivityView {

    private static final int REQUEST_CODE = 1;
    private FloatingActionButton fbtnAddChallenge;
    private RecyclerView rvMain;
    private Toolbar tbMain;
    private ArrayList<ChallengeResponse.Data> challengeResponseList;
    private SwipeRefreshLayout srlMain;
    private ChallengeListAdapter clAdapter;
    private AppBarLayout ablMain;
    private TextView tvTitle;
    private TextView tvTitleCollapsed;
    private ImageView ivProfile;
    private ImageView ivProfileNext;

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


        /* Get Contents From Server... */
        tryGetChallenge();

        /* Toolbar */
        setSupportActionBar(tbMain);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);

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
                .load(sSharedPreferences.getString(KAKAO_PROFILE, ""))
                .placeholder(R.drawable.kakao_default_profile_image)
                .error(R.drawable.kakao_default_profile_image)
                .centerCrop()
                .into(ivProfile);
    }

    @Override
    public void validateSuccess(ArrayList<ChallengeResponse.Data> data) {
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
        Log.i("GET", "getvalidateFauilure");
    }

    @Override
    public void validateDeleteSuccess(int challengeId) {
        hideProgressDialog();
        showToast("Delete Success");

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
                ablMain.setExpanded(true,true);
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
                Log.e("RESULT", "결과 받기 성공");
                // Get Intent from AddChallenge Activity
                AddChallengeResponse.Data datum = (AddChallengeResponse.Data) data.getSerializableExtra("item");
                if (datum != null) {
                    Log.e("RESULT", "결과 받기 성공");
                    ChallengeResponse.Data newDatum = new ChallengeResponse.Data(datum);
                    clAdapter.addItem(newDatum);
                    tvTitle.setText("오늘 도전할 챌린지가\n" + clAdapter.getItemCount() + "개 있습니다");
                    tvTitleCollapsed.setText("오늘의 챌린지: " + clAdapter.getItemCount() + "개");

                    /* Saving to Local DB... */
                    ChallengeModel challengeModel = new ChallengeModel(this);
                    challengeModel.insertDatum(newDatum);
                }
            } else {
                Log.e("RESULT", "결과 받기 실패");
            }
        }
    }

    @Override
    public void onRefresh() {
        tryGetChallenge();
        srlMain.setRefreshing(false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 임시 로그아웃
                new AlertDialog.Builder(this).setMessage("이 버튼은 임시 로그아웃 버튼입니다. 로그아웃하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                                    @Override
                                    public void onCompleteLogout() {
                                        finish();
                                    }
                                });
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();

                return true;
        }
        return false;
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
