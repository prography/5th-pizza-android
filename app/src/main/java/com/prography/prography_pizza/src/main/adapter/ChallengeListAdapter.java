package com.prography.prography_pizza.src.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.RecordActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class ChallengeListAdapter extends RecyclerView.Adapter<ChallengeListAdapter.ViewHolder> {
    final MainActivityView mMainActivityView;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;

    private ArrayList<MainResponse.Data> challengeResponses;
    private String routineType;
    private String objectUnit;
    private String exerciseType;

    public ChallengeListAdapter(ArrayList<MainResponse.Data> challengeResponses, Context context, MainActivityView mMainActivityView) {
        this.challengeResponses = challengeResponses;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.mMainActivityView = mMainActivityView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_challenge_main, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainResponse.Data data = challengeResponses.get(position);

        String time ="";
        if (data != null) {
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
                    time = String.valueOf((int) data.getTime() / 60);
                    objectUnit = "분";
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
            holder.tvCreatedAt.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date())+" 오늘 목표 달성률");
            holder.pbChallenge.setProgress(data.getAchievement());
            holder.tvPercentage.setText(data.getAchievement() + "%");
            holder.tvChallengers.setText("같이 하는 친구들: " + data.getChallengersConut() + "명");

            if (data.getAchievement() == 100) {
                holder.tvClear.setVisibility(View.VISIBLE);
                holder.itemView.setEnabled(false);
            } else {
                holder.tvClear.setVisibility(View.GONE);
                holder.itemView.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return challengeResponses.size();
    }

    public void addItem(MainResponse.Data datum) {
        challengeResponses.add(0, datum);
        notifyItemInserted(0);
        mRecyclerView.smoothScrollToPosition(0);
    }

    public void setItems(ArrayList<MainResponse.Data> data) {
        challengeResponses = data;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        int cid = challengeResponses.get(position).getChallengeId();
        challengeResponses.remove(position);
        notifyItemRemoved(position);
        mMainActivityView.startDeleteProcess(cid);
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvCreatedAt;
        ImageView ivMore;
        TextView tvPercentage;
        ProgressBar pbChallenge;
        TextView tvClear;
        TextView tvChallengers;

        public ViewHolder(final View itemView) {
            super(itemView);

            /* findViewByID */
            tvTitle = itemView.findViewById(R.id.tv_title_item_challenge_main);
            tvCreatedAt = itemView.findViewById(R.id.tv_start_date_item_challenge_main);
            ivMore = itemView.findViewById(R.id.iv_menu_item_challenge);
            tvPercentage = itemView.findViewById(R.id.tv_progress_percentage_item_challenge_main);
            pbChallenge = itemView.findViewById(R.id.pb_item_challenge_main);
            tvClear = itemView.findViewById(R.id.tv_clear_item_challenge_main);
            tvChallengers = itemView.findViewById(R.id.tv_challengers_item_challenge);

            /* Set On Click Listener */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == itemView) {
                        Intent intent = new Intent(v.getContext(), RecordActivity.class);
                        intent.putExtra("challenge", challengeResponses.get(getAdapterPosition()));
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {

        public SwipeToDelete() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            new AlertDialog.Builder(mContext).setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    deleteItem(position);
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    notifyDataSetChanged();
                }
            }).create().show();

        }
    }
}
