package com.prography.prography_pizza.src.tutorial.adapter;

import com.prography.prography_pizza.src.tutorial.fragments.AddChallengeFragment;
import com.prography.prography_pizza.src.tutorial.fragments.MainFragment;
import com.prography.prography_pizza.src.tutorial.fragments.MypageHistoryFragment;
import com.prography.prography_pizza.src.tutorial.fragments.RecordFragment;
import com.prography.prography_pizza.src.tutorial.fragments.SignInFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TutorialPager extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 5;

    public TutorialPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    public TutorialPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment=new SignInFragment();
                break;
            case 1:
                fragment=new MainFragment();
                break;
            case 2:
                fragment=new AddChallengeFragment();
                break;
            case 3:
                fragment=new RecordFragment();
                break;
            case 4:
                fragment=new MypageHistoryFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
