package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.github.vipulasri.timelineview.TimelineView;
import com.prography.prography_pizza.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChallengeDetailExpandableAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.activity_challenge_detail, null);
        TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(view, viewType);
        return timeLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;

        public TimeLineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            mTimelineView = itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }
    }
}
