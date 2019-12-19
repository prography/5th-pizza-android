package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.provider.Settings.System.DATE_FORMAT;

public class ChallengeDetailExpandableAdapter extends RecyclerView.Adapter<ChallengeDetailExpandableAdapter.TimeLineViewHolder> {
    private ArrayList<ChallengeDetailResponse.Data> mList;
    private String routineType;
    private String objectUnit;
    private String exerciseType;

    private Context mContext;
    private RecyclerView mRecyclerView;

    public ChallengeDetailExpandableAdapter(ArrayList<ChallengeDetailResponse.Data> mList, Context context) {
        this.mList = mList;
        mContext = context;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(view, viewType);
        return timeLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        ChallengeDetailResponse.Data timeLineModel = mList.get(position);

        if (timeLineModel != null) {
            routineType = "";
            objectUnit = "";
            exerciseType = "";
            // 변환 필요.
            switch (timeLineModel.getRoutineType()) {
                case "daily":
                    routineType = "매일";
                    break;
                case "weekly":
                    routineType = "매주";
                    break;
                case "monthly":
                    routineType = "매달";
                    break;
            }

            switch (timeLineModel.getObjectUnit()) {
                case "distance":
                    objectUnit = "km";
                    break;
                case "time":
                    objectUnit = "분";
            }

            switch (timeLineModel.getExerciseType()) {
                case "running":
                    exerciseType = "뛰기";
                    break;
                case "cycling":
                    exerciseType = "자전거 타기";
                    break;
            }
            String title = routineType + " " + (int) timeLineModel.getTime() + objectUnit + " " + exerciseType;
            holder.tvTitle.setText(title);

            String date = String.format(DATE_FORMAT); // 날짜 형식 parse
            holder.tvDate.setText(date);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView tvDate;
        TextView tvTitle;


        public TimeLineViewHolder(@NonNull final View itemView, int viewType) {
            super(itemView);

            /* findViewByID */
            mTimelineView = itemView.findViewById(R.id.timeline);
            tvDate = itemView.findViewById(R.id.tv_timeline_date);
            tvTitle = itemView.findViewById(R.id.tv_timeline_title);

            mTimelineView.initLine(viewType);

            /* Set On Click Listener */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==itemView){
                        // show savedRecord
                    }
                }
            });
        }
    }
}
