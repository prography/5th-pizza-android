package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;
import static com.prography.prography_pizza.src.ApplicationClass.DATE_FORMAT;

public class ChallengeDetailExpandableAdapter extends RecyclerView.Adapter<ChallengeDetailExpandableAdapter.TimeLineViewHolder> {
    private ArrayList<ChallengeDetailResponse.Data> mList;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private String date;
    private int recordId;
    private String exerciseType;
    private String runningTime;
    private double distance;

    public ChallengeDetailExpandableAdapter(ArrayList<ChallengeDetailResponse.Data> mList, Context context, String exerciseType) {
        this.mList = mList;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.exerciseType=exerciseType;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_timeline, parent, false);
        TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(view, viewType);
        return timeLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        ChallengeDetailResponse.Data timeLineModel = mList.get(position);

        if (timeLineModel != null) {
            try {
                Date date = DATE_FORMAT.parse(timeLineModel.getCreatedAt());
                holder.tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            recordId = timeLineModel.getRecordId();
            switch (exerciseType) {
                case "running":
                    exerciseType = "러닝";
                    break;
                case "cycling":
                    exerciseType = "사이클";
                    break;
            }
            holder.tvTitle.setText(recordId + "일차 " + exerciseType);

            if (timeLineModel.getTotalDistance() > 1000) {
                distance = timeLineModel.getTotalDistance() / 1000;
                holder.tvDistance.setText(String.format("%.2f", (float) distance) + "km");
            } else {
                distance = timeLineModel.getTotalDistance();
                holder.tvDistance.setText(String.format("%.0f", (float) distance) + "m");
            }

            Date d = new Date(Double.valueOf(timeLineModel.getRunningTime()).longValue());
            runningTime = CURRENT_TIME_FORMAT.format(d);
            holder.tvTime.setText(runningTime);

            double tmp = timeLineModel.getRunningTime() / timeLineModel.getTotalDistance(); // sec/km
            int min = (int) (tmp / 60);
            int sec = (int) (tmp - min * 60);
            holder.tvSpeed.setText(min + "'" + sec + "''/km");

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
        ImageView ivMap;
        TextView tvDate;
        TextView tvTitle;
        TextView tvDistance;
        TextView tvSpeed;
        TextView tvTime;


        public TimeLineViewHolder(@NonNull final View itemView, int viewType) {
            super(itemView);

            /* findViewByID */
            mTimelineView = itemView.findViewById(R.id.timeline);
            ivMap = itemView.findViewById(R.id.iv_map_detail);
            tvDate = itemView.findViewById(R.id.tv_date_detail);
            tvTitle = itemView.findViewById(R.id.tv_title_detail);
            tvDistance = itemView.findViewById(R.id.tv_distance_detail);
            tvSpeed = itemView.findViewById(R.id.tv_speed_detail);
            tvTime = itemView.findViewById(R.id.tv_time_detail);


            mTimelineView.initLine(viewType);

            /* Set On Click Listener */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == itemView) {

                    }
                }
            });
        }
    }
}
