package com.prography.prography_pizza.src.challenge_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.challenge_detail.adapter.ChallengeDetailExpandableAdapter;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;
import com.prography.prography_pizza.src.main.models.MainResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.USER_PROFILE;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class ChallengeDetailActivity extends BaseActivity implements ChallengeDetailActivityView {
    private int mChallengeId;
    private String mExerciseType;
    private RecyclerView rvDetail;
    private Toolbar tbDetail;
    private ArrayList<ChallengeDetailResponse.Data> mRecordList = new ArrayList<>();

    private ChallengeDetailExpandableAdapter cdeAdapter;
//    private TimelineView timelineView;
//    private TextView tvTitle;
//    private TextView tvDate;
    private ImageView ivProfile;
    private TextView tvUserName;

    private MainResponse.Data mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        /* findViewByID */
        rvDetail = findViewById(R.id.rv_record_detail);
        tbDetail = findViewById(R.id.toolbar_detail);
        tvUserName=findViewById(R.id.tv_username_detail);
//        tvTitle=findViewById(R.id.tv_timeline_title);
//        tvDate=findViewById(R.id.tv_timeline_date);

        /* Toolbar */
        setSupportActionBar(tbDetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* Init View */
        tvUserName.setText(sSharedPreferences.getString(USER_NAME, "USERName"));
        Glide.with(this)
                .load(sSharedPreferences.getString(USER_PROFILE, null))
                .centerCrop()
                .error(R.drawable.kakao_default_profile_image)
                .placeholder(R.drawable.kakao_default_profile_image)
                .into(ivProfile);
        ivProfile.setClipToOutline(true);

        /* Get Intent */
        Intent intent = getIntent();
        mData = intent.getParcelableExtra("challenge");
        if (mData != null) {
            mChallengeId = mData.getChallengeId();
            mExerciseType = mData.getExerciseType();
        }

        cdeAdapter = new ChallengeDetailExpandableAdapter(mRecordList, this, mExerciseType, mData);
        rvDetail.setAdapter(cdeAdapter);

        tyrGetDetail(mChallengeId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tyrGetDetail(int mChallengeId) {
        showProgressDialog();
        ChallengeDetailService challengeDetailService = new ChallengeDetailService(this);
        challengeDetailService.getDetail(mChallengeId);
    }

    @Override
    public void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data) {
        hideProgressDialog();
        showToast("Success");
        cdeAdapter.setData(data);
    }

    @Override
    public void validateFailure() {
        hideProgressDialog();
        showToast("Failure");
    }


    @Override
    public void onClick(View v) {

    }

}
