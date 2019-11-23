package com.prography.progrpahy_pizza.src.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.addChallenge.AddChallengeActivity;
import com.prography.progrpahy_pizza.src.main.interfaces.MainActivityView;
import com.prography.progrpahy_pizza.src.main.models.Challenge;
import com.prography.progrpahy_pizza.src.main.models.RecyclerViewAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements MainActivityView, View.OnClickListener {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ArrayList<Challenge> arr1;
    private RecyclerViewAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.btn_main_addChallenge);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar_main);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        floatingActionButton.setOnClickListener(this);


        ArrayList<String> arr = new ArrayList<>();
//        arr.add("hello");
//        arr.add("hi");
//        arr.add("ahh");
//        recyclerView.setAdapter(new RecyclerViewAdapter(arr, this));

        arr1=new ArrayList<>();
        adapter=new RecyclerViewAdapter(arr1, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_edit:


        }

        return true;
    }

    @Override
    public void validateSuccess(String text) {
        hideProgressDialog();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
    }

    @Override
    //floating button
    public void onClick(View v) {
        Intent intent = new Intent(this, AddChallengeActivity.class);
        startActivityForResult(intent, 1);
//        startNextActivity(AddChallengeActivity.class);
//        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if (resultCode == RESULT_OK){
                Log.e("LOG", "결과 받기 성공");
                String routineType=data.getStringExtra("routineType");
                String t=data.getStringExtra("time");
                double time = Integer.parseInt(t);
                String objectUnit=data.getStringExtra("objectUnit");
                String exerciseType=data.getStringExtra("exerciseType");

                Challenge challenge=new Challenge();
                challenge.setRoutineType(routineType);
                challenge.setTime(time);
                challenge.setObjectUnit(objectUnit);
                challenge.setExerciseType(exerciseType);

                adapter.addItem(challenge);

                adapter.notifyItemInserted(adapter.getItemCount()-1);

            }
        }
    }
}
