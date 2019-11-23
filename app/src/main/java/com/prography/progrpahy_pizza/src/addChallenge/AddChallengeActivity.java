package com.prography.progrpahy_pizza.src.addChallenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.fragments.SelectorBottomSheetFragment;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class AddChallengeActivity extends BaseActivity implements AddChallengeActivityView {

    private TextView tvDate;
    private TextView tvTimeOrDistance;
    private TextView tvType;
    private Button btnSubmit;
    private Toolbar tbAddChallenge;

    private String routine_type;
    private double quota;
    private String object_unit;
    private String exerceise_type;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchallenge);

        /* findViewByID */
        tvDate = findViewById(R.id.tv_date_addchallenge);
        tvTimeOrDistance = findViewById(R.id.tv_time_addchallenge);
        tvType = findViewById(R.id.tv_type_addchallenge);
        btnSubmit = findViewById(R.id.btn_challengeAssign);
        tbAddChallenge = findViewById(R.id.toolbar_addchallenge);

        /* Toolbar */
        setSupportActionBar(tbAddChallenge);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);

        /* Set On Click Listener */
        btnSubmit.setOnClickListener(this);
        tvType.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTimeOrDistance.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void postvalidateSuccess() {
        hideProgressDialog();
        showToast("postSuccess");

        //for recyclerView
        Intent intent = new Intent();
        //intent.putExtra("routineType", spnRoutineType.getSelectedItem().toString());
        //intent.putExtra("time", Double.parseDouble(edtTime.getText().toString()));
        //intent.putExtra("objectUnit", spnObjectUnit.getSelectedItem().toString());
        //intent.putExtra("exerciseType", spnExerciseType.getSelectedItem().toString());

        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void postvalidateFailure() {
        hideProgressDialog();
        showToast("postFailure");
    }

    private void tryPostChallenge(String routineType, double time, String objectUnit, String exerciseType) {
        showProgressDialog();
        AddChallengeService addChallengeService = new AddChallengeService(this);

        switch (routineType){
            case "매일":
                routine_type = "daily";
                break;
            case "매주":
                routine_type="weekly";
                break;
            case "매달":
                routine_type="monthly";
                break;
        }

        switch (objectUnit){
            case "분":
            case "시간":
                object_unit="time";
                break;
            case "km":
                object_unit="distance";
                break;
        }

        switch (exerciseType){
            case "러닝":
                exerceise_type="running";
                break;
            case "사이클":
                exerceise_type="cycling";
                break;
        }

        addChallengeService.postChallenge(routine_type, quota, object_unit, exerceise_type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_challengeAssign:
                /*tryPostChallenge(spnRoutineType.getSelectedItem().toString(),
                Double.parseDouble(edtTime.getText().toString()),
                        spnObjectUnit.getSelectedItem().toString(),
                        spnExerciseType.getSelectedItem().toString());*/
                //서버로 post 하고 postvalidateSuccess 하면, mainActivity에 intent 넘겨주고 finish();
                break;
            case R.id.tv_date_addchallenge:
                SelectorBottomSheetFragment selectorBottomSheetFragment = new SelectorBottomSheetFragment();
                selectorBottomSheetFragment.show(getSupportFragmentManager(), "selector");
                break;
            case R.id.tv_type_addchallenge:
                SelectorBottomSheetFragment selectorBottomSheetFragment2 = new SelectorBottomSheetFragment();
                selectorBottomSheetFragment2.show(getSupportFragmentManager(), "selector");
                break;
            case R.id.tv_time_addchallenge:
                SelectorBottomSheetFragment selectorBottomSheetFragment3 = new SelectorBottomSheetFragment();
                selectorBottomSheetFragment3.show(getSupportFragmentManager(), "selector");
                break;
        }
    }
}
