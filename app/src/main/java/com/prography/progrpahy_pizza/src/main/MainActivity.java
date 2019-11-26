package com.prography.progrpahy_pizza.src.main;

import android.os.Bundle;
<<<<<<< Updated upstream
import android.view.Menu;
import android.view.MenuItem;

=======
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
>>>>>>> Stashed changes
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.interfaces.MainActivityView;
import com.prography.progrpahy_pizza.src.main.models.RecyclerViewAdapter;

import java.util.ArrayList;

<<<<<<< Updated upstream
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
=======
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
>>>>>>> Stashed changes
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements MainActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recyclerView);

<<<<<<< Updated upstream
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> arr=new ArrayList<>();
        arr.add("hello");
        arr.add("hi");
        arr.add("ahh");
        recyclerView.setAdapter(new RecyclerViewAdapter(arr, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_edit:
                
        }

        return true;
    }

    @Override
    public void validateSuccess(String text) {
=======
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //menu icon 설정
        actionBar.setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.ic_menu);

        CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.collapseBar);
        collapsingToolbarLayout.setTitle("오늘 도전할 챌린지가 2개 있습니다.");

        floatingActionButton.setOnClickListener(this);

        challengeResponseArrayList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(challengeResponseArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getvalidateSuccess() {
>>>>>>> Stashed changes
        hideProgressDialog();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
    }
}
