package com.prography.prography_pizza.src.mypage.fragments.records;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;
import com.prography.prography_pizza.src.mypage.fragments.records.adapter.MyChallengeListAdapter;

public class RecordsFragment extends BaseFragment {

    private Context mContext;
    private RecyclerView rvChallengeIng;
    private RecyclerView rvChallengeComplete;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records_mypage, container, false);

        /* findViewByID */
        rvChallengeComplete = view.findViewById(R.id.rv_challenge_complete_records_mypage);
        rvChallengeIng = view.findViewById(R.id.rv_challenge_ing_records_mypage);

        /* RecyclerView */
        //rvChallengeIng.setAdapter(new MyChallengeListAdapter(mContext, ));

        return view;
    }


}
