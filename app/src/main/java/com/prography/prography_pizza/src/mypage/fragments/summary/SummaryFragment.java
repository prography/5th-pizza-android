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

import java.util.ArrayList;

import static com.prography.prography_pizza.src.ApplicationClass.KAKAO_USERNAME;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SummaryFragment extends BaseFragment {

    private TextView tvUserName;
    private ImageView ivType;
    private RecyclerView rvBadges;

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

        /* RecyclerView - Badges */
        // dummy
        ArrayList<String> badges = new ArrayList<>();
        badges.add("Badge1");
        badges.add("Badge2");
        badges.add("Badge3");
        badges.add("Badge4");
        badges.add("Badge5");
        badges.add("Badge6");
        badges.add("Badge7");
        badges.add("더보기...");
        badgeListAdapter = new BadgeListAdapter(mContext);
        rvBadges.setAdapter(badgeListAdapter);
        badgeListAdapter.setItems(badges);

        /* init View */
        tvUserName.setText(sSharedPreferences.getString(KAKAO_USERNAME, "UserName"));
        ivType.setBackground(new ShapeDrawable(new OvalShape()));
        ivType.setClipToOutline(true);

        return view;
    }
}
