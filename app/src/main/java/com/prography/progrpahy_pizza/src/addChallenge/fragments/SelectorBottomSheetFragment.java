package com.prography.progrpahy_pizza.src.addChallenge.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
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

    private Dialog mDialog;

    final AddChallengeActivityView mAddChallengeActivityView;

    public SelectorBottomSheetFragment(AddChallengeActivityView mAddChallengeActivityView) {
        this.mAddChallengeActivityView = mAddChallengeActivityView;
    }

    private TextView tvPositive;
    private TextView tvNegative;

    private RecyclerView rvItem1;
    private RecyclerView rvItem2;
    private RecyclerView rvItem3;

    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<String> list3 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selectorbottomsheet, container, false);

        /* findViewByID */
        tvPositive = view.findViewById(R.id.tv_positive_selector_bottomsheet);
        tvNegative = view.findViewById(R.id.tv_negative_selector_bottomsheet);
        rvItem1 = view.findViewById(R.id.rv_selector1_bottomsheet);
        rvItem2 = view.findViewById(R.id.rv_selector2_bottomsheet);
        rvItem3 = view.findViewById(R.id.rv_selector3_bottomsheet);

        /* RecyclerView */
        list1.clear();
        list2.clear();
        list3.clear();
        list1.add("매일");list1.add("매주");list1.add("매달");
        list2.add("30분");list2.add("1시간");list2.add("2시간");list2.add("3시간");
        list2.add("1km");list2.add("2km");list2.add("3km");list2.add("5km");list2.add("10km");
        list3.add("달리기를 하겠다.");list3.add("자전거를 타겠다.");

        rvItem1.setLayoutManager(new PickerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, this, 0));
        rvItem2.setLayoutManager(new PickerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, this, 1));
        rvItem3.setLayoutManager(new PickerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, this, 2));

        rvItem1.setAdapter(new SelectorListAdapter(list1, getActivity(), this, 0));
        rvItem2.setAdapter(new SelectorListAdapter(list2, getActivity(), this, 1));
        rvItem3.setAdapter(new SelectorListAdapter(list3, getActivity(), this, 2));

        new LinearSnapHelper().attachToRecyclerView(rvItem1);
        new LinearSnapHelper().attachToRecyclerView(rvItem2);
        new LinearSnapHelper().attachToRecyclerView(rvItem3);

        /* Set On Click Listener */
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDialog = getDialog();
        View dialogView = mDialog.getWindow().getDecorView().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior.from(dialogView).setHideable(false);
    }


    @Override
    public void onItemClick(int recyclerIndex, final int position) {
        switch (recyclerIndex) {
            case 0:
                rvItem1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvItem1.smoothScrollToPosition(position);
                    }
                }, 100);
                break;
            case 1:
                rvItem2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvItem2.smoothScrollToPosition(position);
                    }
                }, 100);
                break;
            case 2:
                rvItem3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvItem3.smoothScrollToPosition(position);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void onItemSelected(int recyclerIndex, int position) {
        switch (recyclerIndex) {
            case 0:
                mAddChallengeActivityView.onPickerItemSelected(recyclerIndex, list1.get(position));
                break;
            case 1:
                mAddChallengeActivityView.onPickerItemSelected(recyclerIndex, list2.get(position));
                break;
            case 2:
                mAddChallengeActivityView.onPickerItemSelected(recyclerIndex, list3.get(position));
                break;
        }
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
}
