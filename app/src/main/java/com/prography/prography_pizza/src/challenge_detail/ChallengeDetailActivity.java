package com.prography.prography_pizza.src.challenge_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.challenge_detail.adapter.ChallengeDetailExpandableAdapter;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class ChallengeDetailActivity extends BaseActivity implements ChallengeDetailActivityView {
    private int mChallengeId;
    private RecyclerView rvDetail;
    private Toolbar tbDetail;
    private ArrayList<ChallengeDetailResponse.Data> mList=new ArrayList<>();
    private ChallengeDetailExpandableAdapter cdAdapter;
    private TimelineView timelineView;
    private TextView tvTitle;
    private TextView tvDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        /* findViewByID */
        rvDetail=findViewById(R.id.recyclerView_detail);
        tbDetail=findViewById(R.id.toolbar_detail);
//        tvTitle=findViewById(R.id.tv_timeline_title);
//        tvDate=findViewById(R.id.tv_timeline_date);

        /* Toolbar */
        setSupportActionBar(tbDetail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        /* RecyclerView */
        mList.add(new ChallengeDetailResponse.Data(1,"running",612000,1200,"2019-12-19"));
        mList.add(new ChallengeDetailResponse.Data(2,"running",306000,600,"2019-12-20"));

        cdAdapter=new ChallengeDetailExpandableAdapter(mList,this);
        rvDetail.setAdapter(cdAdapter);

        /* Get Intent */
        Intent intent = getIntent();
        mChallengeId = intent.getIntExtra("challengeId", 0);


    }

    @Override
    public void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data) {

    }

    @Override
    public void validateFailure() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
