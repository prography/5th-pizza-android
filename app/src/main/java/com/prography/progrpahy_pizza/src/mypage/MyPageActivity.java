package com.prography.progrpahy_pizza.src.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.mypage.interfaces.MyPageActivityView;

public class MyPageActivity extends BaseActivity implements MyPageActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
    }
}
