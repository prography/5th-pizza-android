package com.prography.prography_pizza.src.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.add_challenge.AddChallengeActivity;
import com.prography.prography_pizza.src.main.adapter.ChallengeListPager;
import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.mypage.MyPageActivity;
import com.prography.prography_pizza.src.record.RecordActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class MainActivity extends BaseActivity implements MainActivityView {

    public static final int REQUEST_CODE = 1;
    private FloatingActionButton fbtnAddChallenge;
    private ArrayList<MainResponse.Data> challengeResponseList;
    private TextView tvTitle;
    private ImageView ivProfile;
    private ImageView ivProfileNext;
    private ViewPager2 vpMain;
    private MaterialButton mbtnStart;
    private DotsIndicator diMain;

    private ChallengeListPager clPager;


    public static Activity sMainActivity;
    public static float dpUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* findViewByID */
        fbtnAddChallenge = findViewById(R.id.fbtn_add_challenge_main);
        tvTitle = findViewById(R.id.tv_title_expanded_main);
        ivProfile = findViewById(R.id.iv_profile_expanded_main);
        ivProfileNext = findViewById(R.id.iv_next_profile_main);
        vpMain = findViewById(R.id.vp_main);
        mbtnStart = findViewById(R.id.mbtn_add_challenge_main);
        diMain = findViewById(R.id.di_main);

        /* Set static Activity */
        sMainActivity = this;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dpUnit = displayMetrics.density;

        /* Get Contents From Server... */
        tryGetChallenge();

        /* ViewPager2 */
        challengeResponseList = new ArrayList<>();
        clPager = new ChallengeListPager(this, this, challengeResponseList);
        vpMain.setAdapter(clPager);
        vpMain.setPageTransformer(clPager);
        vpMain.setOffscreenPageLimit(3);
        /* Dots Indicator */
        diMain.setViewPager2(vpMain);

        /* Set On Click Listener */
        mbtnStart.setOnClickListener(this);
        fbtnAddChallenge.setOnClickListener(this);
        ivProfileNext.setOnClickListener(this);
        ivProfile.setOnClickListener(this);


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
        clPager.setData(challengeResponseList);
        tvTitle.setText("오늘 도전할 챌린지가\n" + clPager.getItemCount() + "개 있습니다");

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

        /* Set View */
        diMain.setViewPager2(vpMain);
        tvTitle.setText("오늘 도전할 챌린지가\n" + clPager.getItemCount() + "개 있습니다");
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
            case R.id.mbtn_add_challenge_main:
                Intent intent1 = new Intent(this, RecordActivity.class);
                intent1.putExtra("challenge", challengeResponseList.get(vpMain.getCurrentItem()));
                startActivityForResult(intent1, REQUEST_CODE);
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
}
