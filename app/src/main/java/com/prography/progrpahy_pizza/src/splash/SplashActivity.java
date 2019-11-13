package com.prography.progrpahy_pizza.src.splash;

import android.os.Bundle;

import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.record.RecordActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNextActivity(RecordActivity.class);
        finish();


    }
}
