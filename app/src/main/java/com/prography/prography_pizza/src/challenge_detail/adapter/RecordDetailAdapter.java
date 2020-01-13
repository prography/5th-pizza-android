package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;
import com.prography.prography_pizza.src.common.utils.GlideApp;
import com.prography.prography_pizza.src.main.models.MainResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.prography.prography_pizza.src.ApplicationClass.BASE_FIREBASE_STORAGE;
import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;
import static com.prography.prography_pizza.src.ApplicationClass.DATE_FORMAT;

public class RecordDetailAdapter extends RecyclerView.Adapter<RecordDetailAdapter.TimeLineViewHolder> {
    private ArrayList<ChallengeDetailResponse.Data> mRecordList;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private String date;
    private int recordId;
    private String exerciseType;
    private String runningTime;
    private double distance;

    private MainResponse.Data mChallenge;

    public RecordDetailAdapter(Context context, ArrayList<ChallengeDetailResponse.Data> mList, String exerciseType, MainResponse.Data data) {
        this.mRecordList = mList;
        mContext = context;
        this.exerciseType = exerciseType;
        mChallenge = data;
    }

    public void setData(ArrayList<ChallengeDetailResponse.Data> data) {
        mRecordList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
        TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(view, viewType);
        return timeLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        ChallengeDetailResponse.Data timeLineModel = mRecordList.get(position);

        if (timeLineModel != null) {

            switch (exerciseType) {
                case "running":
                    exerciseType = "러닝";
                    break;
                case "cycling":
                    exerciseType = "사이클";
                    break;
            }

            try {
                Date cur = DATE_FORMAT.parse(timeLineModel.getCreatedAt());
                Date start = DATE_FORMAT.parse(mChallenge.getCreatedAt());
                Date dist = new Date(cur.getTime() - start.getTime());
                Calendar distCal = Calendar.getInstance();
                distCal.setTime(dist);

                holder.tvTitle.setText(distCal.get(Calendar.DAY_OF_YEAR) + "일차 " + exerciseType);
                holder.tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(cur));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (timeLineModel.getTotalDistance() > 1000) {
                distance = timeLineModel.getTotalDistance() / 1000;
                holder.tvDistance.setText(String.format("%.2f", (float) distance) + "km");
            } else {
                distance = timeLineModel.getTotalDistance();
                holder.tvDistance.setText(String.format("%.0f", (float) distance) + "m");
            }

            Date d = new Date(Double.valueOf(timeLineModel.getRunningTime() * 1000).longValue());
            runningTime = CURRENT_TIME_FORMAT.format(d);
            holder.tvTime.setText(runningTime);

            int pace = (int) ((timeLineModel.getRunningTime() / timeLineModel.getTotalDistance()) * 1000); // sec/m -> sec/km
            holder.tvSpeed.setText(String.format("%2d'%2d'' /km", pace / 60, pace % 60));

            // Image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("imgs").child(timeLineModel.getRecordImgUrl());
            GlideApp.with(mContext)
                    .load(ref)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .thumbnail(0.3f)
                    .into(holder.ivMap);

        }
    }


    @Override
    public int getItemCount() {
        return mRecordList.size();
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
