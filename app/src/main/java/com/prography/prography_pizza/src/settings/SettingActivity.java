package com.prography.prography_pizza.src.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.settings.interfaces.SettingActivityView;

public class SettingActivity extends BaseActivity implements SettingActivityView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
