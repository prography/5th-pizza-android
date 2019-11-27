package com.prography.progrpahy_pizza.src.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;
import com.prography.progrpahy_pizza.src.record.RecordActivity;

import java.util.ArrayList;


public class ChallengeListAdapter extends RecyclerView.Adapter<ChallengeListAdapter.ViewHolder> {
    private ArrayList<ChallengeResponse.Data> challengeResponses;
    private LayoutInflater layoutInflater;

    public ChallengeListAdapter(ArrayList<ChallengeResponse.Data> challengeResponses, Context context) {
        this.challengeResponses = challengeResponses;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_challenge_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChallengeResponse.Data data = challengeResponses.get(position);
        if (data != null) {
            // 변환 필요.
            String title = data.getExerciseType() + data.getTime() + data.getObjectUnit() + data.getRoutineType();
            holder.tvTitle.setText(title);
            holder.tvCreatedAt.setText(data.getCreatedAt());
            // holder.pbChallenge
            // holder.tvPercentage
            // holder.ivMore
        }
    }

    @Override
    public int getItemCount() {
        return challengeResponses.size();
    }

    public void addItem(ChallengeResponse.Data datum) {
        challengeResponses.add(datum);
        notifyItemInserted(getItemCount() - 1);
    }

    public void setItems(ArrayList<ChallengeResponse.Data> data) {
        challengeResponses = data;
        notifyDataSetChanged();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvCreatedAt;
        ImageView ivMore;
        TextView tvPercentage;
        ProgressBar pbChallenge;

        public ViewHolder(final View itemView) {
            super(itemView);

            /* findViewByID */
            tvTitle = itemView.findViewById(R.id.tv_title_item_challenge_main);
            tvCreatedAt = itemView.findViewById(R.id.tv_start_date_item_challenge_main);
            ivMore = itemView.findViewById(R.id.iv_menu_item_challenge);
            tvPercentage = itemView.findViewById(R.id.tv_progress_percentage_item_challenge_main);
            pbChallenge = itemView.findViewById(R.id.pb_item_challenge_main);

            /* Set On Click Listener */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == itemView) {
                        Intent intent = new Intent(v.getContext(), RecordActivity.class);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
