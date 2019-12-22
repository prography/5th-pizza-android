package com.prography.prography_pizza.src.record.fragments.current;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseFragment;
import com.prography.prography_pizza.src.record.RecordActivity;
import com.prography.prography_pizza.src.record.adapter.RecordPagerAdapter;
import com.prography.prography_pizza.src.record.fragments.current.interfaces.CurrentFragmentView;

public class CurrentFragment extends BaseFragment implements CurrentFragmentView {

    private Context mContext;

    private TextView tvDistance;
    private TextView tvDistanceUnit;
    private TextView tvDistanceDesc;
    private TextView tvTime;
    private TextView tvTimeUnit;
    private TextView tvTimeDesc;
    private TextView tvProgress;
    private TextView tvProgressUnit;
    private TextView tvProgressDesc;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_current_record, container, false);

        /* findViewByID */
        tvDistance = view.findViewById(R.id.tv_distance_current_record);
        tvDistanceUnit = view.findViewById(R.id.tv_distance_unit_current_record);
        tvDistanceDesc = view.findViewById(R.id.tv_distance_desc_current_record);
        tvTime = view.findViewById(R.id.tv_time_current_record);
        tvTimeUnit = view.findViewById(R.id.tv_time_unit_current_record);
        tvTimeDesc = view.findViewById(R.id.tv_time_desc_current_record);
        tvProgress = view.findViewById(R.id.tv_progress_current_record);
        tvProgressUnit = view.findViewById(R.id.tv_progress_unit_current_record);
        tvProgressDesc = view.findViewById(R.id.tv_progress_desc_current_record);

        /* Get Bundle & Init View */
        Bundle bundle = getArguments();
        switch (bundle.getInt("type")) {
            case RecordPagerAdapter.TYPE_CURRENT:
                tvDistance.setText("0.0");
                tvDistanceDesc.setText("진행 거리");
                tvTime.setText("00:00");
                tvTimeDesc.setText("진행 시간");
                tvTimeUnit.setText("");
                tvProgress.setText("00'00''");
                tvProgressDesc.setText("페이스");
                tvProgressUnit.setText("");
                break;
            case RecordPagerAdapter.TYPE_LEFT:
                int goalType = bundle.getInt("goalType");
                if (goalType == RecordActivity.GOALTYPE_DISTANCE) {
                    if (bundle.getDouble("goal") < 1000) {
                        tvDistance.setText(String.format("%.1f", (float) (bundle.getDouble("goal"))));
                        tvDistanceUnit.setText("m");
                    } else {
                        tvDistance.setText(String.format("%.1f", (float) (bundle.getDouble("goal")) / 1000));
                        tvDistanceUnit.setText("km");
                    }
                    tvTime.setText("--");
                } else {
                    tvDistance.setText("-.-");
                    tvTime.setText(String.valueOf((int) (bundle.getDouble("goal") / 60 / 1000)));
                }
                tvDistanceDesc.setText("남은 거리");
                tvTimeDesc.setText("남은 시간");
                tvTimeUnit.setText("min");
                tvProgress.setText("0.0");
                tvProgressDesc.setText("달성률");
                tvProgressUnit.setText("%");
                break;
        }

        return view;
    }

    @Override
    public void setCurrentView(String distance, String distanceUnit, String time, String progress) {
        tvDistance.setText(distance);
        tvDistanceUnit.setText(distanceUnit);
        tvTime.setText(time);
        tvProgress.setText(progress);
    }
}
