package com.prography.prography_pizza.src.mypage.fragments.summary;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;
import com.prography.prography_pizza.src.mypage.fragments.summary.adapter.BadgeListAdapter;
import com.prography.prography_pizza.src.mypage.fragments.summary.interfaces.SummaryFragmentView;
import com.prography.prography_pizza.src.mypage.fragments.summary.models.BadgeResponse;

import java.util.ArrayList;

import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SummaryFragment extends BaseFragment implements SummaryFragmentView {

    private TextView tvUserName;
    private ImageView ivType;
    private RecyclerView rvBadges;
    private TextView tvNoBadges;

    private Context mContext;
    private BadgeListAdapter badgeListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_mypage, container, false);

        /* findViewByID */
        tvUserName = view.findViewById(R.id.tv_username_summary_mypage);
        ivType = view.findViewById(R.id.iv_type_img_summary_mypage);
        rvBadges = view.findViewById(R.id.rv_badges_summary_mypage);
        tvNoBadges = view.findViewById(R.id.tv_no_badges_summary_mypage);

        /* RecyclerView - Badges */
        // dummy
        badgeListAdapter = new BadgeListAdapter(mContext);
        rvBadges.setAdapter(badgeListAdapter);

        /* init View */
        tvUserName.setText(sSharedPreferences.getString(USER_NAME, "UserName"));

        tryGetBadges();

        return view;
    }

    public void tryGetBadges() {
        final SummaryService summaryService = new SummaryService(this);
        summaryService.tryGetBadges();
    }

    @Override
    public void validateSuccess(ArrayList<BadgeResponse.Data> badges) {
        badgeListAdapter.setItems(badges);
        if (badges.size() == 0) {
            tvNoBadges.setVisibility(View.VISIBLE);
            rvBadges.setVisibility(View.GONE);
        } else {
            tvNoBadges.setVisibility(View.GONE);
            rvBadges.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void validateFailure() {
        showSimpleMessageDialog(getString(R.string.network_error));
    }
}
