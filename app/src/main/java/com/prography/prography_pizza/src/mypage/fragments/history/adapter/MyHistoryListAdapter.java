package com.prography.prography_pizza.src.mypage.fragments.history.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.challenge_detail.ChallengeDetailActivity;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.mypage.MyPageActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.prography.prography_pizza.src.ApplicationClass.DATE_FORMAT;

public class MyHistoryListAdapter extends RecyclerView.Adapter<MyHistoryListAdapter.ChallengeViewHolder> {

    private ArrayList<MainResponse.Data> mChallenges;
    private Context mContext;

    public MyHistoryListAdapter(Context context, ArrayList<MainResponse.Data> mChallenges) {
        this.mChallenges = mChallenges;
        mContext = context;
    }

    public void setData(ArrayList<MainResponse.Data> data) {
        mChallenges = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_challenge_main, parent, false);
        ChallengeViewHolder viewHolder = new ChallengeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        MainResponse.Data data = mChallenges.get(position);

        if (data != null) {
            String routineType = "";
            String objectUnit = "";
            String exerciseType = "";
            String time = "";
            // 변환 필요.
            switch (data.getRoutineType()) {
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

            switch (data.getObjectUnit()) {
                case "distance":
                    objectUnit = "km";
                    time = String.valueOf((int) data.getTime() / 1000);
                    break;
                case "time":
                    objectUnit = "분";
                    time = String.valueOf((int) data.getTime() / 60);
            }

            switch (data.getExerciseType()) {
                case "running":
                    exerciseType = "뛰기";
                    break;
                case "cycling":
                    exerciseType = "자전거 타기";
                    break;
            }
            String title = routineType + " " + time + objectUnit + " " + exerciseType;
            holder.tvTitle.setText(title);

            try {
                Date date = DATE_FORMAT.parse(data.getCreatedAt());
                holder.tvCreatedAt.setText(new SimpleDateFormat("yyyy.MM.dd").format(date) + " 부터 지금까지");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (data.getAchievement() < 100) {
                holder.pbChallenge.setProgress(data.getAchievement());
                holder.tvPercentage.setText(data.getAchievement() + "%");
            } else {
                holder.pbChallenge.setVisibility(View.INVISIBLE);
                holder.pbChallenge.setVisibility(View.INVISIBLE);
            }
            // holder.ivMore
            holder.tvChallengers.setText(data.getChallengersConut() + "명");
        }
    }

    @Override
    public int getItemCount() {
        return mChallenges.size();
    }

    public class ChallengeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvCreatedAt;
        ImageView ivMore;
        TextView tvPercentage;
        ProgressBar pbChallenge;
        TextView tvChallengers;

        public ChallengeViewHolder(@NonNull final View itemView) {
            super(itemView);

            /* findViewByID */
            tvTitle = itemView.findViewById(R.id.tv_title_item_challenge_main);
            tvCreatedAt = itemView.findViewById(R.id.tv_start_date_item_challenge_main);
            ivMore = itemView.findViewById(R.id.iv_menu_item_challenge);
            tvPercentage = itemView.findViewById(R.id.tv_progress_percentage_item_challenge_main);
            pbChallenge = itemView.findViewById(R.id.pb_item_challenge_main);
            tvChallengers = itemView.findViewById(R.id.tv_challengers_item_challenge);

            /* Set On Click Listener */
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                // Start Challenge Detail Activity
                Intent intent = new Intent(v.getContext(), ChallengeDetailActivity.class);
                intent.putExtra("challenge", mChallenges.get(getAdapterPosition()));
                ((MyPageActivity) v.getContext()).startActivity(intent);
            }
        }
    }
}
