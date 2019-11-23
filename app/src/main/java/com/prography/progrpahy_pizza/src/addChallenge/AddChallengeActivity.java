package com.prography.progrpahy_pizza.src.addChallenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;

public class AddChallengeActivity extends BaseActivity implements AddChallengeActivityView {

    private Spinner spnRoutineType;
    private EditText edtTime;
    private Spinner spnObjectUnit;
    private Spinner spnExerciseType;
    private Button btn_addChallenge;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchallenge);

        edtTime = findViewById(R.id.time);
        spnRoutineType = findViewById(R.id.spinner_routineType);
        spnObjectUnit = findViewById(R.id.spinner_objectUnit);
        spnExerciseType = findViewById(R.id.spinner_exerciseType);
        btn_addChallenge = findViewById(R.id.btn_challengeAssign);


        /* Set On Click Listener */
        btn_addChallenge.setOnClickListener(this);

    }

    @Override
    public void postvalidateSuccess() {
        hideProgressDialog();
        showToast("postSuccess");

        //for recyclerView
        Intent intent = new Intent();
        intent.putExtra("routineType", spnRoutineType.getSelectedItem().toString());
        intent.putExtra("time", Double.parseDouble(edtTime.getText().toString()));
        intent.putExtra("objectUnit", spnObjectUnit.getSelectedItem().toString());
        intent.putExtra("exerciseType", spnExerciseType.getSelectedItem().toString());

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
        addChallengeService.postChallenge(routineType, time, objectUnit, exerciseType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_challengeAssign:
                tryPostChallenge(spnRoutineType.getSelectedItem().toString(),
                        Double.parseDouble(edtTime.getText().toString()),
                        spnObjectUnit.getSelectedItem().toString(),
                        spnExerciseType.getSelectedItem().toString());
                //서버로 post 하고 postvalidateSuccess 하면, mainActivity에 intent 넘겨주고 finish();
        }
    }
}
