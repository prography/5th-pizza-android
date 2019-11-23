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
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;
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
    private ArrayList<ChallengeResponse> challengeResponseArrayList;
    private RecyclerViewAdapter adapter;

    private String routineType;
    private double time;
    private String objectUnit;
    private String exerciseType;

    private static final int REQUEST_CODE = 1;

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

        challengeResponseArrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(challengeResponseArrayList, this);
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
    public void getvalidateSuccess() {
        hideProgressDialog();
        Log.i("GET", "getvalidateSuccess");
    }

    @Override
    public void getvalidateFailure() {
        hideProgressDialog();
        Log.i("GET", "getvalidateFauilure");
    }

    private void tryGetChallenge(){
        showProgressDialog();
        MainService mainService=new MainService(this);
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

                routineType = data.getStringExtra("routineType");
                String t = data.getStringExtra("time");
                time = Double.parseDouble(t);
                objectUnit = data.getStringExtra("objectUnit");
                exerciseType = data.getStringExtra("exerciseType");

                ChallengeResponse challengeResponse = new ChallengeResponse(routineType, time, objectUnit, exerciseType);

                adapter.addItem(challengeResponse);

                adapter.notifyItemInserted(adapter.getItemCount() - 1);

            } else {
                Log.e("LOG", "결과 받기 실패");
            }
        }
    }
}
