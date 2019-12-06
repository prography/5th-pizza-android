package com.prography.prography_pizza.src.mypage.fragments.summary;

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

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;

import static com.prography.prography_pizza.src.ApplicationClass.KAKAO_USERNAME;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class SummaryFragment extends BaseFragment {

    private TextView tvUserName;
    private ImageView ivType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary_mypage, container, false);

        /* findViewByID */
        tvUserName = view.findViewById(R.id.tv_username_summary_mypage);
        ivType = view.findViewById(R.id.iv_type_img_summary_mypage);


        /* init View */
        tvUserName.setText(sSharedPreferences.getString(KAKAO_USERNAME, "UserName"));
        ivType.setBackground(new ShapeDrawable(new OvalShape()));
        ivType.setClipToOutline(true);

        return view;
    }
}
