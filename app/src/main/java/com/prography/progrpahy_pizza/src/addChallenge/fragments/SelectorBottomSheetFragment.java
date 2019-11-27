package com.prography.progrpahy_pizza.src.addChallenge.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.addChallenge.fragments.adapter.PickerLayoutManager;
import com.prography.progrpahy_pizza.src.addChallenge.fragments.adapter.SelectorListAdapter;
import com.prography.progrpahy_pizza.src.addChallenge.fragments.interfaces.SelectorBotomSheetFragmentView;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;

import java.util.ArrayList;

public class SelectorBottomSheetFragment extends BottomSheetDialogFragment implements SelectorBotomSheetFragmentView {

    final AddChallengeActivityView mAddChallengeActivityView;
    ArrayList<RecyclerView> rvPickerLists = new ArrayList<>();
    private TextView tvPositive;
    private TextView tvNegative;
    private ArrayList<ArrayList<String>> mPickerLists;
    private boolean isPositiveButton = true;
    private boolean isNegativeButton = false;

    private SelectorBottomSheetFragment(Builder builder) {
        mAddChallengeActivityView = builder.addChallengeActivityView;
        mPickerLists = builder.pickerLists;
        this.isPositiveButton = builder.isPositiveButton;
        this.isNegativeButton = builder.isNegativeButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectorbottomsheet, container, false);

        /* findViewByID */
        tvPositive = view.findViewById(R.id.tv_positive_selector_bottomsheet);
        tvNegative = view.findViewById(R.id.tv_negative_selector_bottomsheet);
        rvPickerLists.clear();
        rvPickerLists.add((RecyclerView) view.findViewById(R.id.rv_selector1_bottomsheet));
        rvPickerLists.add((RecyclerView) view.findViewById(R.id.rv_selector2_bottomsheet));
        rvPickerLists.add((RecyclerView) view.findViewById(R.id.rv_selector3_bottomsheet));


        /* RecyclerView */
        for (int i = 0; i < rvPickerLists.size(); i++) {
            rvPickerLists.get(i).setLayoutManager(new PickerLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false, this, i));
            rvPickerLists.get(i).setAdapter(new SelectorListAdapter(mPickerLists.get(i), getActivity(), this, i));
        }

        /* Set On Click Listener */
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);


        /* Init View */
        if (!isPositiveButton) {
            tvPositive.setVisibility(View.INVISIBLE);
        }
        if (!isNegativeButton) {
            tvNegative.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        View dialogView = getDialog().getWindow().getDecorView().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        BottomSheetBehavior.from(dialogView).setHideable(false);
    }


    @Override
    public void onItemClick(final int recyclerIndex, final int position) {
        rvPickerLists.get(recyclerIndex).postDelayed(new Runnable() {
            @Override
            public void run() {
                rvPickerLists.get(recyclerIndex).smoothScrollToPosition(position);
            }
        }, 100);
    }

    @Override
    public void onItemSelected(int recyclerIndex, int position) {
        mAddChallengeActivityView.onPickerItemSelected(recyclerIndex, mPickerLists.get(recyclerIndex).get(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_positive_selector_bottomsheet:
                dismiss();
                mAddChallengeActivityView.onPickerPositiveClick();
                break;
            case R.id.tv_negative_selector_bottomsheet:
                dismiss();
                break;
        }
    }

    public static class Builder {
        private AddChallengeActivityView addChallengeActivityView;
        private ArrayList<ArrayList<String>> pickerLists = new ArrayList<>();
        private boolean isPositiveButton = true;
        private boolean isNegativeButton = true;

        public Builder(AddChallengeActivityView addChallengeActivityView) {
            this.addChallengeActivityView = addChallengeActivityView;
        }

        public Builder setPickerLists(ArrayList<ArrayList<String>> pickerLists) {
            this.pickerLists = pickerLists;
            return this;
        }

        public Builder setPositiveButton(boolean option) {
            this.isPositiveButton = option;
            return this;
        }

        public Builder setNegativeButon(boolean option) {
            this.isNegativeButton = option;
            return this;
        }

        public SelectorBottomSheetFragment build() {
            return new SelectorBottomSheetFragment(this);
        }
    }
}
