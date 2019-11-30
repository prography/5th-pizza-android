package com.prography.progrpahy_pizza.src.addChallenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.fragments.SelectorBottomSheetFragment;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;
import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class AddChallengeActivity extends BaseActivity implements AddChallengeActivityView {

    private TextView tvDate;
    private TextView tvTimeOrDistance;
    private TextView tvType;
    private Button btnSubmit;
    private Toolbar tbAddChallenge;

    private String mRoutineType;
    private double mQuota;
    private String mObjectUnit;
    private String mExerceiseType;

    private SelectorBottomSheetFragment selectorBottomSheetFragment;

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

        /* BottomSheetFragment */
        ArrayList<ArrayList<String>> pickerLists = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        list1.add("매일");
        list1.add("매주");
        list1.add("매달");
        list2.add("30분");
        list2.add("1시간");
        list2.add("2시간");
        list2.add("3시간");
        list2.add("1km");
        list2.add("2km");
        list2.add("3km");
        list2.add("5km");
        list2.add("10km");
        list3.add("달리기를 하겠다.");
        list3.add("자전거를 타겠다.");
        pickerLists.add(list1);
        pickerLists.add(list2);
        pickerLists.add(list3);
        selectorBottomSheetFragment = new SelectorBottomSheetFragment
                .Builder(this)
                .setPickerLists(pickerLists)
                .setPositiveButton(true)
                .setNegativeButon(true)
                .build();

        /* Set On Click Listener */
        btnSubmit.setOnClickListener(this);
        tvType.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTimeOrDistance.setOnClickListener(this);

        /* Init View */
        selectorBottomSheetFragment.show(getSupportFragmentManager(), "selector");
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
    public void postvalidateSuccess(AddChallengeResponse.Data datum) {
        hideProgressDialog();
        showToast("postSuccess");
        Intent intent = new Intent();
        intent.putExtra("item", datum);
        setResult(RESULT_OK, intent);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
        finish();
    }

    @Override
    public void postvalidateFailure() {
        hideProgressDialog();
        showToast("postFailure");
    }

    @Override
    public void onPickerItemSelected(int which, String selected) {
        switch (which) {
            case 0:
                tvDate.setText(selected);
                break;
            case 1:
                tvTimeOrDistance.setText(selected);
                break;
            case 2:
                tvType.setText(selected);
                break;
        }
    }

    @Override
    public void onPickerPositiveClick() {
        // Server Connecting...
        tryPostChallenge(tvDate.getText().toString(), tvTimeOrDistance.getText().toString(), tvType.getText().toString());
    }

    public void tryPostChallenge(String dateType, String timeOrDistance, String exerciseType) {
        showProgressDialog();
        AddChallengeService addChallengeService = new AddChallengeService(this);

        switch (dateType) {
            case "매일":
                mRoutineType = "daily";
                break;
            case "매주":
                mRoutineType = "weekly";
                break;
            case "매달":
                mRoutineType = "monthly";
                break;
        }

        switch (timeOrDistance) {
            case "30분":
            case "1시간":
            case "2시간":
            case "3시간":
                mObjectUnit = "time";
                int minIdx = timeOrDistance.lastIndexOf('분');
                int hourIdx = timeOrDistance.lastIndexOf('시');
                if (minIdx != -1) {
                    mQuota = Double.parseDouble(timeOrDistance.substring(0, minIdx));
                } else if (hourIdx != -1) {
                    mQuota = Double.parseDouble(timeOrDistance.substring(0, hourIdx)) * 60;
                }
                break;
            case "1km":
            case "2km":
            case "3km":
            case "5km":
            case "10km":
                mObjectUnit = "distance";
                mQuota = Double.parseDouble(timeOrDistance.substring(0, timeOrDistance.lastIndexOf('k')));
                break;
        }

        switch (exerciseType) {
            case "달리기를 하겠다.":
                mExerceiseType = "running";
                break;
            case "자전거를 타겠다.":
                mExerceiseType = "cycling";
                break;
        }

        addChallengeService.postChallenge(mRoutineType, mQuota, mObjectUnit, mExerceiseType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_challengeAssign:
                if (tvDate.getText().toString().equals("") || tvTimeOrDistance.getText().toString().equals("") || tvType.getText().toString().equals("")) {
                    new AlertDialog.Builder(this)
                            .setMessage("입력값을 다시 확인해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create()
                            .show();
                    return;
                }
                // Server Connecting...
                tryPostChallenge(tvDate.getText().toString(), tvTimeOrDistance.getText().toString(), tvType.getText().toString());
                break;
            case R.id.tv_date_addchallenge:
            case R.id.tv_type_addchallenge:
            case R.id.tv_time_addchallenge:
                selectorBottomSheetFragment.show(getSupportFragmentManager(), "selector");
                break;
        }
    }
}
