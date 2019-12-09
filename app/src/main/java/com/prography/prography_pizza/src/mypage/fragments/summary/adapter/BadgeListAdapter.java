package com.prography.prography_pizza.src.mypage.fragments.summary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;

import java.util.ArrayList;

public class BadgeListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> mBadges = new ArrayList<>();

    public BadgeListAdapter(Context context) {
        mContext = context;
    }

    public void setItems(ArrayList<String> badges) {
        mBadges = badges;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_badge_mypage, parent, false);

        BadgeViewHolder badgeViewHolder = new BadgeViewHolder(view);
        return badgeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title = mBadges.get(position);
        if (title != null) {
            ((BadgeViewHolder) holder).tvTitle.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return mBadges.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title_badge);
        }
    }
}
