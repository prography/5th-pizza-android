package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;
import com.prography.prography_pizza.src.common.utils.GlideApp;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private ArrayList<RankResponse.Data> mRankList;
    private Context mContext;

    public RankAdapter(Context mContext, ArrayList<RankResponse.Data> mRankList) {
        this.mRankList = mRankList;
        this.mContext = mContext;
    }

    public void setData(ArrayList<RankResponse.Data> rankList) {
        mRankList = rankList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rank, parent, false);
        RankViewHolder rankViewHolder = new RankViewHolder(view);
        return rankViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankResponse.Data rankModel = mRankList.get(position);
        if (rankModel != null) {
            if (rankModel.getRank() % 2 == 1) {
                holder.itemView.setBackgroundColor(Color.rgb(255, 240, 196));
            }
            holder.tvRank.setText(String.valueOf(rankModel.getRank()));
            holder.tvUserName.setText(rankModel.getUserName());
            Glide.with(mContext).load(rankModel.getProfileUrl()).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(holder.ivProfile);
            holder.tvPercent.setText(rankModel.getProgress() + "%");
            if (rankModel.getProgress() == 100) {
                holder.tvPercent.setTextColor(Color.rgb(255, 160, 71));
            }
            holder.pbDetail.setProgress(rankModel.getProgress());
        }
    }

    @Override
    public int getItemCount() {
        return mRankList.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank;
        TextView tvUserName;
        ImageView ivProfile;
        ProgressBar pbDetail;
        TextView tvPercent;

        public RankViewHolder(@NonNull final View itemView) {
            super(itemView);

            /* findViewByID */
            tvRank = itemView.findViewById(R.id.tv_rank_item_rank);
            tvUserName = itemView.findViewById(R.id.tv_username_item_rank);
            ivProfile = itemView.findViewById(R.id.iv_profile_item_rank);
            pbDetail = itemView.findViewById(R.id.pb_item_rank);
            tvPercent = itemView.findViewById(R.id.tv_percent_item_rank);

        }
    }
}
