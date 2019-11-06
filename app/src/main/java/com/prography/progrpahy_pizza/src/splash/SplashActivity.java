package com.prography.progrpahy_pizza.src.splash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.main.MainActivity;
import com.prography.progrpahy_pizza.src.record.RecordActivity;
import com.prography.progrpahy_pizza.src.signin.SignInActivity;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startNextActivity(RecordActivity.class);
        finish();


    }
}
