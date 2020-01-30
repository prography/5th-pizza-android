package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;
import static com.prography.prography_pizza.src.ApplicationClass.DATE_FORMAT;

public class RecordDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChallengeDetailResponse.Data> mRecordList;
    private ArrayList<Integer> VIEWTYPE;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private String date;
    private int recordId;
    private String exerciseType;
    private String runningTime;
    private double distance;
    // private int VIEWTYPE = 0;
    private final int PARENT_VIEW = 0;
    private final int CHILD_VIEW = 1;
    private ParentVH parentVH;
    private ChildVH childVH;

    private MainResponse.Data mChallenge;

    public RecordDetailAdapter(Context context, ArrayList<ChallengeDetailResponse.Data> mList, String exerciseType, MainResponse.Data data) {
        Log.i("rdAdapter", String.valueOf(mList.size()));
        this.mRecordList = mList;
        mContext = context;
        this.exerciseType = exerciseType;
        mChallenge = data;

        VIEWTYPE = new ArrayList<>(mRecordList.size());
    }

    public void setData(ArrayList<ChallengeDetailResponse.Data> data) {
        mRecordList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case PARENT_VIEW:
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
                viewHolder = new ParentVH(view1);
                break;
            case CHILD_VIEW:
                View view2 = LayoutInflater.from(mContext).inflate(R.layout.item_timeline_expanded, parent, false);
                viewHolder = new ChildVH(view2);
                break;
        }


        //TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChallengeDetailResponse.Data timeLineModel = mRecordList.get(position);

        if (holder instanceof ParentVH) {
            parentVH = (ParentVH) holder;

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

                    parentVH.tvTitle.setText(distCal.get(Calendar.DAY_OF_YEAR) + "일차 " + exerciseType);
                    parentVH.tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(cur));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (timeLineModel.getTotalDistance() > 1000) {
                    distance = timeLineModel.getTotalDistance() / 1000;
                    parentVH.tvDistance.setText(String.format("%.2f", (float) distance) + "km");
                } else {
                    distance = timeLineModel.getTotalDistance();
                    parentVH.tvDistance.setText(String.format("%.0f", (float) distance) + "m");
                }

                Date d = new Date(Double.valueOf(timeLineModel.getRunningTime() * 1000).longValue());
                runningTime = CURRENT_TIME_FORMAT.format(d);
                parentVH.tvTime.setText(runningTime);

                int pace = (int) ((timeLineModel.getRunningTime() / timeLineModel.getTotalDistance()) * 1000); // sec/m -> sec/km
                parentVH.tvSpeed.setText(String.format("%2d'%2d'' /km", pace / 60, pace % 60));

                // Image
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("imgs").child(timeLineModel.getRecordImgUrl());
                GlideApp.with(mContext)
                        .load(ref)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .thumbnail(0.3f)
                        .into(parentVH.ivMap);

            }
        } else if (holder instanceof ChildVH) {
            childVH = (ChildVH) holder;

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

                    childVH.tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(cur));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (timeLineModel.getTotalDistance() > 1000) {
                    distance = timeLineModel.getTotalDistance() / 1000;
                    childVH.tvDistance.setText(String.format("%.2f", (float) distance) + "km");
                } else {
                    distance = timeLineModel.getTotalDistance();
                    childVH.tvDistance.setText(String.format("%.0f", (float) distance) + "m");
                }

                Date d = new Date(Double.valueOf(timeLineModel.getRunningTime() * 1000).longValue());
                runningTime = CURRENT_TIME_FORMAT.format(d);
                childVH.tvTime.setText(runningTime);

                int pace = (int) ((timeLineModel.getRunningTime() / timeLineModel.getTotalDistance()) * 1000); // sec/m -> sec/km
                childVH.tvSpeed.setText(String.format("%2d'%2d'' /km", pace / 60, pace % 60));

                // Image
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("imgs").child(timeLineModel.getRecordImgUrl());
                GlideApp.with(mContext)
                        .load(ref)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .thumbnail(0.3f)
                        .into(childVH.ivMap);

            }
        }
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE.get(position);
    }

    public int getTimelineViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class ParentVH extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        ImageView ivMap;
        TextView tvDate;
        TextView tvTitle;
        TextView tvDistance;
        TextView tvSpeed;
        TextView tvTime;

        public ParentVH(@NonNull final View itemView) {
            super(itemView);

            /* findViewByID */
            mTimelineView = itemView.findViewById(R.id.timeline);
            ivMap = itemView.findViewById(R.id.iv_map_detail);
            tvDate = itemView.findViewById(R.id.tv_date_detail);
            tvTitle = itemView.findViewById(R.id.tv_title_detail);
            tvDistance = itemView.findViewById(R.id.tv_distance_detail);
            tvSpeed = itemView.findViewById(R.id.tv_speed_detail);
            tvTime = itemView.findViewById(R.id.tv_time_detail);

            mTimelineView.initLine(getTimelineViewType(getAdapterPosition()));

            /* Set On Click Listener */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == itemView) {
                        VIEWTYPE.set(getAdapterPosition(), 1);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public class ChildVH extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        ImageView ivMap;
        TextView tvDate;
        TextView tvDistance;
        TextView tvSpeed;
        TextView tvTime;

        public ChildVH(@NonNull final View itemView) {
            super(itemView);

            /* findViewByID */
            mTimelineView = itemView.findViewById(R.id.timelineExpanded_detail);
            ivMap = itemView.findViewById(R.id.iv_mapExpanded_detail);
            tvDate = itemView.findViewById(R.id.tv_dateExpanded_detail);
            tvDistance = itemView.findViewById(R.id.tv_distanceExpanded_detail);
            tvSpeed = itemView.findViewById(R.id.tv_speedExpanded_detail);
            tvTime = itemView.findViewById(R.id.tv_timeExpanded_detail);


            mTimelineView.initLine(getTimelineViewType(getAdapterPosition()));

            /* Set On Click Listener */
            itemView.setOnClickListener(v -> {
                if (v == itemView) {
                    VIEWTYPE.set(getAdapterPosition(), 0);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
