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
import com.prography.prography_pizza.db.ChallengeModel;
import com.prography.prography_pizza.src.BaseFragment;
import com.prography.prography_pizza.src.common.utils.RecyclerViewDecoration;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.mypage.fragments.records.adapter.MyChallengeListAdapter;
import com.prography.prography_pizza.src.mypage.fragments.records.interfaces.RecordsFragmentView;

import java.util.ArrayList;

public class RecordsFragment extends BaseFragment implements RecordsFragmentView {

    private Context mContext;
    private RecyclerView rvChallengeIng;
    private RecyclerView rvChallengeComplete;

    private MyChallengeListAdapter mclAdapterIng;
    private MyChallengeListAdapter mclAdapterComplete;


    private ArrayList<MainResponse.Data> mChallengeDataIng = new ArrayList<>();
    private ArrayList<MainResponse.Data> mChallengeDataCompleted = new ArrayList<>();

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

        /* From Local DB... */
        ChallengeModel challengeModel = new ChallengeModel(mContext);
        mChallengeDataIng = challengeModel.getAllUnCompleted();
        mChallengeDataCompleted = challengeModel.getAllCompleted();

        /* RecyclerView */
        mclAdapterIng = new MyChallengeListAdapter(mContext, mChallengeDataIng);
        rvChallengeIng.setAdapter(mclAdapterIng);
        rvChallengeIng.addItemDecoration(new RecyclerViewDecoration(30));
        mclAdapterComplete = new MyChallengeListAdapter(mContext, mChallengeDataCompleted);
        rvChallengeComplete.setAdapter(mclAdapterComplete);
        rvChallengeComplete.addItemDecoration(new RecyclerViewDecoration(30));

        return view;
    }


}
