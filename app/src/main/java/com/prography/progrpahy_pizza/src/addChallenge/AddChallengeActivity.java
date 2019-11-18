package com.prography.progrpahy_pizza.src.addChallenge;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;

import androidx.annotation.Nullable;

public class AddChallengeActivity extends BaseActivity {
    private Spinner spnRoutineType;
    private EditText edtTime;
    private Spinner spnObjectUnit;
    private Spinner spnExerciseType;
//    private ArrayAdapter arrRoutineType, arrObjectUnit, arrExerciseType;
    private Button btn_addChallenge;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchallenge);

        spnRoutineType=findViewById(R.id.spinner_routineType);
        edtTime=findViewById(R.id.time);
        spnObjectUnit=findViewById(R.id.spinner_objectUnit);
        spnExerciseType=findViewById(R.id.spinner_exerciseType);
        btn_addChallenge=findViewById(R.id.btn_challengeAssign);

        spnRoutineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String routineType= (String) spnRoutineType.getItemAtPosition(position);
//                Toast.makeText(AddChallengeActivity.this,"선택된 아이템 : "+spnRoutineType.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnObjectUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String objectUnit= (String) spnObjectUnit.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_addChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_challengeAssign:

                }




                startNextActivity(MainActivity.class);
                finish();
            }
        });

    }
}
