package com.prography.prography_pizza.src.mypage.fragments.summary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.mypage.fragments.summary.models.BadgeResponse;

import java.util.ArrayList;

public class BadgeListAdapter extends RecyclerView.Adapter<BadgeListAdapter.BadgeViewHolder> {

    private Context mContext;
    private ArrayList<BadgeResponse.Data> mBadges = new ArrayList<>();

    public BadgeListAdapter(Context context) {
        mContext = context;
    }

    public void setItems(ArrayList<BadgeResponse.Data> badges) {
        mBadges = badges;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_badge_mypage, parent, false);

        BadgeViewHolder badgeViewHolder = new BadgeViewHolder(view);
        return badgeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        BadgeResponse.Data badge = mBadges.get(position);
        if (badge != null) {
            switch (badge.getBadgeType()) {
                case "RunningAccumulativeDistance":
                    switch (badge.getBadgeLevel()) {
                        case 1:
                            holder.ivBadge.setImageResource(R.drawable.ic_run_level1);
                            holder.tvTitle.setText("3km 달리기 성공");
                            break;
                        case 2:
                            holder.ivBadge.setImageResource(R.drawable.ic_run_level2);
                            holder.tvTitle.setText("5km 달리기 성공");
                            break;
                        case 3:
                            holder.ivBadge.setImageResource(R.drawable.ic_run_level3);
                            holder.tvTitle.setText("7km 달리기 성공");
                            break;
                        case 4:
                            holder.ivBadge.setImageResource(R.drawable.ic_run_level4);
                            holder.tvTitle.setText("10km 달리기 성공");
                            break;
                        case 5:
                            holder.ivBadge.setImageResource(R.drawable.ic_run_level5);
                            holder.tvTitle.setText("25km 달리기 성공");
                            break;

                    }
                    break;
                case "CycleAccumulativeTime":
                case "RunningAccumulativeTime":
                    switch (badge.getBadgeLevel()) {
                        case 1:
                            holder.ivBadge.setImageResource(R.drawable.ic_time_level1);
                            holder.tvTitle.setText("6시간 달리기 성공");
                            break;
                        case 2:
                            holder.ivBadge.setImageResource(R.drawable.ic_time_level2);
                            holder.tvTitle.setText("12시간 달리기 성공");
                            break;
                        case 3:
                            holder.ivBadge.setImageResource(R.drawable.ic_time_level3);
                            holder.tvTitle.setText("18시간 달리기 성공");
                            break;
                        case 4:
                            holder.ivBadge.setImageResource(R.drawable.ic_time_level4);
                            holder.tvTitle.setText("24시간 달리기 성공");
                            break;
                        case 5:
                            holder.ivBadge.setImageResource(R.drawable.ic_time_level5);
                            holder.tvTitle.setText("72시간 달리기 성공");
                            break;

                    }
                    break;
                case "SuccessChallenge":
                    switch (badge.getBadgeLevel()) {
                        case 1:
                            holder.ivBadge.setImageResource(R.drawable.ic_success_level1);
                            holder.tvTitle.setText("챌린지 1개 성공");
                            break;
                        case 2:
                            holder.ivBadge.setImageResource(R.drawable.ic_success_level2);
                            holder.tvTitle.setText("챌린지 5개 성공");
                            break;
                        case 3:
                            holder.ivBadge.setImageResource(R.drawable.ic_success_level3);
                            holder.tvTitle.setText("챌린지 10개 성공");
                            break;
                        case 4:
                            holder.ivBadge.setImageResource(R.drawable.ic_success_level4);
                            holder.tvTitle.setText("챌린지 15개 성공");
                            break;
                        case 5:
                            holder.ivBadge.setImageResource(R.drawable.ic_success_level5);
                            holder.tvTitle.setText("챌린지 20개 성공");
                            break;

                    }
                    break;
                case "ContinuousRecording":
                    switch (badge.getBadgeLevel()) {
                        case 1:
                            holder.ivBadge.setImageResource(R.drawable.ic_continue_level1);
                            holder.tvTitle.setText("연속 기록 3일 성공");
                            break;
                        case 2:
                            holder.ivBadge.setImageResource(R.drawable.ic_continue_level2);
                            holder.tvTitle.setText("연속 기록 6일 성공");
                            break;
                        case 3:
                            holder.ivBadge.setImageResource(R.drawable.ic_continue_level3);
                            holder.tvTitle.setText("연속 기록 9일 성공");
                            break;
                        case 4:
                            holder.ivBadge.setImageResource(R.drawable.ic_continue_level4);
                            holder.tvTitle.setText("연속 기록 12일 성공");
                            break;
                        case 5:
                            holder.ivBadge.setImageResource(R.drawable.ic_continue_level5);
                            holder.tvTitle.setText("연속 기록 15일 성공");
                            break;

                    }
                    break;
                case "CycleAccumulativeDistance":
                    switch (badge.getBadgeLevel()) {
                        case 1:
                            holder.ivBadge.setImageResource(R.drawable.ic_cycle_level1);
                            holder.tvTitle.setText("3km 자전거 타기 성공");
                            break;
                        case 2:
                            holder.ivBadge.setImageResource(R.drawable.ic_cycle_level2);
                            holder.tvTitle.setText("5km 자전거 타기 성공");
                            break;
                        case 3:
                            holder.ivBadge.setImageResource(R.drawable.ic_cycle_level3);
                            holder.tvTitle.setText("7km 자전거 타기 성공");
                            break;
                        case 4:
                            holder.ivBadge.setImageResource(R.drawable.ic_cycle_level4);
                            holder.tvTitle.setText("10km 자전거 타기 성공");
                            break;
                        case 5:
                            holder.ivBadge.setImageResource(R.drawable.ic_cycle_level5);
                            holder.tvTitle.setText("25km 자전거 타기 성공");
                            break;

                    }
                    break;
            }
            //((BadgeViewHolder) holder).tvTitle.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return mBadges.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBadge;
        TextView tvTitle;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBadge = itemView.findViewById(R.id.iv_img_badge);
            tvTitle = itemView.findViewById(R.id.tv_title_badge);
        }
    }
}
