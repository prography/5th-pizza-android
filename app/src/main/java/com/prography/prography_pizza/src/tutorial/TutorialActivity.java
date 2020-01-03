package com.prography.prography_pizza.src.tutorial;

import android.os.Bundle;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.tutorial.adapter.TutorialPager;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends BaseActivity {
    private FragmentPagerAdapter fragmentPagerAdapter;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        /* findViewByID */
        viewPager=findViewById(R.id.vp_tutorial);
        circleIndicator=findViewById(R.id.ci_tutorial);

        /* Init View */
        fragmentPagerAdapter=new TutorialPager(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        circleIndicator.setViewPager(viewPager);
    }
}
