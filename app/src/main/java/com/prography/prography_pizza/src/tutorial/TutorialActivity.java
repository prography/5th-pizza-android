package com.prography.prography_pizza.src.tutorial;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.signin.SignInActivity;
import com.prography.prography_pizza.src.tutorial.adapter.TutorialPager;
import com.prography.prography_pizza.src.tutorial.interfaces.TutorialView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends BaseActivity implements TutorialView {
    private FragmentPagerAdapter fragmentPagerAdapter;

    private ViewPager vpTutorial;
    private CircleIndicator cindicator;
    private TextView tvSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        /* findViewByID */
        vpTutorial =findViewById(R.id.vp_tutorial);
        cindicator =findViewById(R.id.ci_tutorial);
        tvSkip=findViewById(R.id.tv_skip_tutorial);

        /* Init View */
        fragmentPagerAdapter=new TutorialPager(getSupportFragmentManager());
        vpTutorial.setAdapter(fragmentPagerAdapter);
        cindicator.setViewPager(vpTutorial);

        /* Set On Click Listener */
        tvSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startNextActivity(SignInActivity.class);
        finish();
    }

}
