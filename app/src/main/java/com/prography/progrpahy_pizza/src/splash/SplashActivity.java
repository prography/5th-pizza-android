package com.prography.progrpahy_pizza.src.splash;

import android.os.Bundle;

import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;

import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNextActivity(MainActivity.class);
        finish();
    }
}
