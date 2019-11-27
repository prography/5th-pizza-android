package com.prography.progrpahy_pizza.src.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.AddChallengeActivity;
import com.prography.progrpahy_pizza.src.addChallenge.AddChallengeService;
import com.prography.progrpahy_pizza.src.main.interfaces.MainActivityView;
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;
import com.prography.progrpahy_pizza.src.main.adapter.RecyclerViewAdapter;
import com.prography.progrpahy_pizza.src.main.adapter.RecyclerViewDecoration;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends BaseActivity implements MainActivityView {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ArrayList<ChallengeResponse.Data> challengeResponseArrayList;
    private SwipeRefreshLayout srlMain;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout srlMain;
    private AppBarLayout ablMain;
    private TextView tvTitle;
    private ImageView ivProfile;


    private String mRoutineType;
    private String mQuota;
    private String mExerciseType;

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.btn_main_addChallenge);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar_main);

        srlMain=findViewById(R.id.srl_main);
        ablMain=findViewById(R.id.abl_main);
        tvTitle=findViewById(R.id.tv_title_expanded_main);
        ivProfile=findViewById(R.id.iv_profile_expanded_main);

        srlMain.setOnRefreshListener(this);
        ablMain.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.ic_menu_white);

        floatingActionButton.setOnClickListener(this);

        challengeResponseArrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(challengeResponseArrayList, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerViewDecoration(30));

        srlMain.setOnRefreshListener(this);
    }

    @Override
    public void validateSuccess(ArrayList<ChallengeResponse.Data> data) {
        hideProgressDialog();
        challengeResponseArrayList = data;
        //adapter.
        Log.i("GET", "getvalidateSuccess");
    }

    @Override
    public void validateFailure() {
        hideProgressDialog();
        Log.i("GET", "getvalidateFauilure");
    }

    private void tryGetChallenge() {
        showProgressDialog();
        MainService mainService = new MainService(this);
        mainService.getChallege();
    }

    @Override
    //floating button
    public void onClick(View v) {
        Intent intent = new Intent(this, AddChallengeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.e("LOG", "결과 받기 성공");

                mRoutineType = data.getStringExtra("mRoutineType"); // "매일" "매달" "매주"
                mQuota = data.getStringExtra("mQuota"); //"30분" "1km"
                mExerciseType = data.getStringExtra("mExerciseType"); // "달리기를 하겠다" "자전거를 타겠다"

                String uniform = mRoutineType + " " + mQuota + " " + mExerciseType;


                //adapter.addItem(uniform);

                adapter.notifyItemInserted(adapter.getItemCount() - 1);

            } else {
                Log.e("LOG", "결과 받기 실패");
            }
        }
    }

    public ChallengeResponse.Data translateStringIntoResponseData(String dateType, String timeOrDistance, String exerciseType) {
        String routineType;
        String objectUnit;
        double quota;
        String type;

        switch (dateType) {
            case "매일":
                routineType = "daily";
                break;
            case "매주":
                routineType = "weekly";
                break;
            case "매달":
                routineType = "monthly";
                break;
        }

        switch (timeOrDistance) {
            case "30분":
            case "1시간":
            case "2시간":
            case "3시간":
                objectUnit = "time";
                int minIdx = timeOrDistance.lastIndexOf('분');
                int hourIdx = timeOrDistance.lastIndexOf('시');
                if (minIdx != -1) {
                    quota = Double.parseDouble(timeOrDistance.substring(0, minIdx));
                } else if (hourIdx != -1) {
                    quota = Double.parseDouble(timeOrDistance.substring(0, hourIdx)) * 60;
                }
                break;
            case "1km":
            case "2km":
            case "3km":
            case "5km":
            case "10km":
                objectUnit = "distance";
                quota = Double.parseDouble(timeOrDistance.substring(0, timeOrDistance.lastIndexOf('k')));
                break;
        }

        switch (exerciseType) {
            case "달리기를 하겠다.":
                type = "running";
                break;
            case "자전거를 타겠다.":
                type = "cycling";
                break;
        }


    }

    @Override
    public void onRefresh() {
        srlMain.setRefreshing(false);



    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if(Math.abs(verticalOffset)==appBarLayout.getTotalScrollRange()) {
            // Collapsed
            tvTitle.setVisibility(View.INVISIBLE);
            ivProfile.setVisibility(View.INVISIBLE);
        } else if (verticalOffset == 0) {
            // Expanded
            tvTitle.setVisibility(View.VISIBLE);
            ivProfile.setVisibility(View.VISIBLE);
        }
    }
}
