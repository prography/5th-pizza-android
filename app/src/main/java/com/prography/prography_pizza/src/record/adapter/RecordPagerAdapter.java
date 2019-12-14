package com.prography.prography_pizza.src.record.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prography.prography_pizza.src.challenge_detail.ChallengeDetailActivity;
import com.prography.prography_pizza.src.main.adapter.ChallengeListAdapter;
import com.prography.prography_pizza.src.mypage.MyPageActivity;
import com.prography.prography_pizza.src.mypage.fragments.records.adapter.MyChallengeListAdapter;
import com.prography.prography_pizza.src.record.RecordActivity;
import com.prography.prography_pizza.src.record.fragments.CurrentFragment;

public class RecordPagerAdapter extends FragmentStatePagerAdapter {

    public static final int TYPE_CURRENT = 10;
    public static final int TYPE_LEFT = 11;

    private double mGoal = 0.0;
    private int mGoalType = RecordActivity.GOALTYPE_DISTANCE;


    public RecordPagerAdapter(@NonNull FragmentManager fm, int goalType, double goal) {
        super(fm);
        mGoalType = goalType;
        mGoal = goal;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                fragment = new CurrentFragment();
                bundle.putInt("type", TYPE_LEFT);
                bundle.putInt("goalType", mGoalType);
                bundle.putDouble("goal", mGoal);
                break;
            case 1:
                fragment = new CurrentFragment();
                bundle.putInt("type", TYPE_CURRENT);
                bundle.putInt("goalType", mGoalType);
                bundle.putDouble("goal", mGoal);
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
