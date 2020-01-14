package com.prography.prography_pizza.src.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MypageHistoryFragment extends BaseFragment {
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_tutorial, container, false);

        /* findViewByID */
        imageView=view.findViewById(R.id.iv_history_tutorial);

        return view;
    }
}