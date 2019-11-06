package com.prography.progrpahy_pizza.src.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.interfaces.MainActivityView;
import com.prography.progrpahy_pizza.src.main.models.RecyclerViewAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity implements MainActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.recyclerView);

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
        hideProgressDialog();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
    }
}
