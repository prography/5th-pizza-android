package com.prography.progrpahy_pizza.src.addChallenge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;

import androidx.annotation.Nullable;

public class AddChallengeActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spnRoutineType;
    private EditText edtTime;
    private Spinner spnObjectUnit;
    private Spinner spnExerciseType;
    //    private ArrayAdapter arrRoutineType, arrObjectUnit, arrExerciseType;
    private Button btn_addChallenge;

    private String routineType;
    private String time;
    private String objectUnit;
    private String exerciseType;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchallenge);

        spnRoutineType = findViewById(R.id.spinner_routineType);
        edtTime = findViewById(R.id.time);
        spnObjectUnit = findViewById(R.id.spinner_objectUnit);
        spnExerciseType = findViewById(R.id.spinner_exerciseType);
        btn_addChallenge = findViewById(R.id.btn_challengeAssign);


        spnRoutineType.setOnItemSelectedListener(this);
        spnObjectUnit.setOnItemSelectedListener(this);
        spnExerciseType.setOnItemSelectedListener(this);


        btn_addChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_challengeAssign:
                        time = edtTime.getText().toString();


                        //for recyclerView
                        Intent intent = new Intent();
                        intent.putExtra("routineType", routineType);
                        intent.putExtra("time", time);
                        intent.putExtra("objectUnit", objectUnit);
                        intent.putExtra("exerciseType", exerciseType);

                        setResult(RESULT_OK, intent);
                        finish();

                    default:
                        setResult(RESULT_CANCELED);
                }

            }
        });

    }

    public void postvalidateSuccess() {
        hideProgressDialog();
        Log.i("POST", "postvalidateSuccess");
    }

    public void postvalidateFailure() {
        hideProgressDialog();
        Log.i("POST", "postvalidateFailure");
    }

    private void tryPostChallenge(String routineType, double time, String objectUnit, String exerciseType) {
        showProgressDialog();
        AddChallengeService addChallengeService=new AddChallengeService((AddChallengeActivityView) this);
        addChallengeService.postChallenge(routineType, time, objectUnit, exerciseType);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        routineType = (String) spnRoutineType.getItemAtPosition(position);
        objectUnit = (String) spnObjectUnit.getItemAtPosition(position);
        exerciseType = (String) spnExerciseType.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
