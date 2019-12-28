package com.prography.prography_pizza.src.challenge_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.challenge_detail.adapter.ChallengeDetailExpandableAdapter;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChallengeDetailActivity extends BaseActivity implements ChallengeDetailActivityView {
    private int mChallengeId;
    private String mExerciseType;
    private RecyclerView rvDetail;
    private Toolbar tbDetail;
    private ArrayList<ChallengeDetailResponse.Data> mList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private ChallengeDetailExpandableAdapter cdAdapter;
    private TimelineView timelineView;
    private TextView tvTitle;
    private TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        /* findViewByID */
        rvDetail = findViewById(R.id.recyclerView_detail);
        tbDetail = findViewById(R.id.toolbar_detail);
//        tvTitle=findViewById(R.id.tv_timeline_title);
//        tvDate=findViewById(R.id.tv_timeline_date);

        /* Toolbar */
        setSupportActionBar(tbDetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* RecyclerView */
        /*mList.add(new ChallengeDetailResponse.Data(1,"running",612000,1200,"2019-12-20T00:00:00.000Z"));
        mList.add(new ChallengeDetailResponse.Data(2,"running",306000,600,"2019-12-21T00:00:00.000Z"));
*/
        // RecyclerView 역순으로 출력
       /* mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rvDetail.setLayoutManager(mLayoutManager);*/

        /* Get Intent */
        Intent intent = getIntent();
        mChallengeId = intent.getIntExtra("challengeId", 0);
        mExerciseType= intent.getStringExtra("exerciseType");


        cdAdapter = new ChallengeDetailExpandableAdapter(mList, this, mExerciseType);
        rvDetail.setAdapter(cdAdapter);


        tyrgetDetail(mChallengeId);

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

    private void tyrgetDetail(int mChallengeId) {
        showProgressDialog();
        ChallengeDetailService challengeDetailService = new ChallengeDetailService(this);
        challengeDetailService.getDetail(mChallengeId);
    }

    @Override
    public void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data) {
        hideProgressDialog();
        showToast("Success");
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
