package com.prography.progrpahy_pizza.src.addChallenge;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;

import androidx.annotation.Nullable;

public class AddChallengeActivity extends BaseActivity {
    private Spinner spnRoutineType, spnObjectUnit, spnExerciseType;
    private ArrayAdapter arrRoutineType, arrObjectUnit, arrExerciseType;
    private Button btn_addChallenge;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchallenge);

        spnRoutineType=findViewById(R.id.spinner_routineType);
        spnObjectUnit=findViewById(R.id.spinner_objectUnit);
        spnExerciseType=findViewById(R.id.spinner_exerciseType);
        btn_addChallenge=findViewById(R.id.btn_challengeAssign);

//        arrRoutineType=new ArrayAdapter<>(getApplicationContext(), R.layout.);

        /*spnRoutineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        btn_addChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(MainActivity.class);
                finish();
            }
        });

    }
}
