package com.prography.prography_pizza.src.challenge_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private ArrayList<RankResponse.Data> mRankList;
    private Context mContext;

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rank, parent, false);
        RankViewHolder rankViewHolder = new RankViewHolder(view, viewType);
        return rankViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankResponse.Data rankModel = mRankList.get(position);

        if (rankModel != null) {

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

        public RankViewHolder(@NonNull final View itemView, int viewType) {
            super(itemView);

            /* findViewByID */
            tvRank = itemView.findViewById(R.id.tv_rank_detail);
            tvUserName = itemView.findViewById(R.id.tv_username_detail);
            ivProfile = itemView.findViewById(R.id.iv_profile_detail);
            pbDetail = itemView.findViewById(R.id.progressBar_detail);
            tvPercent = itemView.findViewById(R.id.tv_percent_detail);

        }
    }
}
