package com.prography.progrpahy_pizza.src.splash;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;
import com.prography.progrpahy_pizza.src.signin.SignInActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNextActivity(SignInActivity.class);
        finish();
    }
}
