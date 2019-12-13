package com.prography.prography_pizza.src.record.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.prography.prography_pizza.src.record.fragments.CurrentFragment;

public class RecordPagerAdapter extends FragmentStatePagerAdapter {

    public static final int TYPE_CURRENT = 0;
    public static final int TYPE_LEFT = 1;


    public RecordPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
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
                break;
            case 1:
                fragment = new CurrentFragment();
                bundle.putInt("type", TYPE_CURRENT);
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
