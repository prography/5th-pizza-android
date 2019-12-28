package com.prography.prography_pizza.src.mypage.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prography.prography_pizza.src.mypage.fragments.history.HistoryFragment;
import com.prography.prography_pizza.src.mypage.fragments.summary.SummaryFragment;

public class MyPagePager extends FragmentStatePagerAdapter {


    public MyPagePager(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MyPagePager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new SummaryFragment();
                break;
            case 1:
                fragment = new HistoryFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
