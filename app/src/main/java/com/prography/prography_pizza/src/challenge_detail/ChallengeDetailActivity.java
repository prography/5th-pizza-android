package com.prography.prography_pizza.src.challenge_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.challenge_detail.adapter.RankAdapter;
import com.prography.prography_pizza.src.challenge_detail.adapter.RecordDetailAdapter;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;
import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;
import com.prography.prography_pizza.src.main.models.MainResponse;

import java.util.ArrayList;

public class ChallengeDetailActivity extends BaseActivity implements ChallengeDetailActivityView {
    private int mChallengeId;
    private String mExerciseType;
    private RecyclerView rvRecordDetail;
    private RecyclerView rvRankDetail;
    private Toolbar tbDetail;

    private RecordDetailAdapter rdAdapter;
    private RankAdapter rAdapter;

    private MainResponse.Data mData;
    private ArrayList<ChallengeDetailResponse.Data> mRecordList = new ArrayList<>();
    private ArrayList<RankResponse.Data> mRankList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        /* findViewByID */
        rvRecordDetail = findViewById(R.id.rv_record_detail);
        tbDetail = findViewById(R.id.toolbar_detail);
        rvRankDetail = findViewById(R.id.rv_rank_detail);

        /* Toolbar */
        setSupportActionBar(tbDetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* Init View */

        /* Get Intent */
        Intent intent = getIntent();
        mData = intent.getParcelableExtra("challenge");
        if (mData != null) {
            mChallengeId = mData.getChallengeId();
            mExerciseType = mData.getExerciseType();
        }

        /* RecyclerView - Record */
        rdAdapter = new RecordDetailAdapter(this, mRecordList,  mExerciseType, mData);
        rvRecordDetail.setAdapter(rdAdapter);

        /* RecyclerView - Rank */
        // Dummy
        mRankList.add(new RankResponse.Data(1, "여누YEONU", "", 100));
        mRankList.add(new RankResponse.Data(2, "여누YEONU", "", 90));
        mRankList.add(new RankResponse.Data(3, "여누YEONU", "", 80));
        rAdapter = new RankAdapter(this, mRankList);
        rvRankDetail.setAdapter(rAdapter);

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

    private void tryGetRank(int mChallengeId) {
        showProgressDialog();
        ChallengeDetailService challengeDetailService = new ChallengeDetailService(this);
        //getRank
    }

    @Override
    public void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data) {
        hideProgressDialog();
        rdAdapter.setData(data);
    }

    @Override
    public void validateFailure() {
        hideProgressDialog();
        showSimpleMessageDialog(getString(R.string.network_error));
    }


    @Override
    public void onClick(View v) {

    }

}
