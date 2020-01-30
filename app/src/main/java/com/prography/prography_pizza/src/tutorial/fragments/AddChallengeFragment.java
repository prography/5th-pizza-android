package com.prography.prography_pizza.src.tutorial.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;

public class AddChallengeFragment extends BaseFragment {
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addchallenge_tutorial, container, false);

        /* findViewByID */
        imageView=view.findViewById(R.id.iv_addChallenge_tutorial);

        return view;
    }
}
