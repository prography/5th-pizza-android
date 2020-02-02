package com.prography.prography_pizza.src.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.main.MainActivity;
import com.prography.prography_pizza.src.main.interfaces.MainActivityView;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.RecordActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.prography.prography_pizza.src.ApplicationClass.DATE_FORMAT;

public class ChallengeListPager extends RecyclerView.Adapter<ChallengeListPager.ChallengeListViewHolder> implements ViewPager2.PageTransformer {

    private Context mContext;
    private MainActivityView mMainActivityView;
    private ArrayList<MainResponse.Data> mData;

    private boolean INIT_VIEWPAGER = false;

    public ChallengeListPager(Context mContext, MainActivityView activityView, ArrayList<MainResponse.Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mMainActivityView = activityView;
    }

    public void setData(ArrayList<MainResponse.Data> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        int cid = mData.get(position).getChallengeId();
        mData.remove(position);
        notifyDataSetChanged();
        mMainActivityView.startDeleteProcess(cid);
    }

    @NonNull
    @Override
    public ChallengeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_main, parent, false);

        return new ChallengeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeListViewHolder holder, int position) {

        MainResponse.Data datum = mData.get(position);
        String time = "";
        String routineType = "";
        String objectUnit = "";
        String exerciseType = "";
        if (datum != null) {
            switch (datum.getBaseChallengeData().getRoutineType()) {
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

            switch (datum.getBaseChallengeData().getObjectUnit()) {
                case "distance":
                    objectUnit = "km";
                    time = String.valueOf((int) datum.getBaseChallengeData().getTime() / 1000);
                    break;
                case "time":
                    time = String.valueOf((int) datum.getBaseChallengeData().getTime() / 60);
                    objectUnit = "분";
            }

            switch (datum.getBaseChallengeData().getExerciseType()) {
                case "running":
                    exerciseType = "뛰기";
                    holder.ivType.setImageResource(R.drawable.iv_run_main);
                    break;
                case "cycling":
                    exerciseType = "자전거 타기";
                    holder.ivType.setImageResource(R.drawable.iv_cycle_main);
                    break;
            }
            String title = routineType + " " + time + objectUnit + " " + exerciseType;
            holder.tvTitle.setText(title);
            holder.tvDate.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date())+" 오늘 목표 달성률");
            holder.pbCard.setProgress(datum.getAchievement());
            holder.tvProgress.setText(datum.getAchievement() + "%");
            holder.tvChallengesCnt.setText("같이 도전하는 친구들 " + datum.getChallengersConut() + "명");
            try {
                holder.tvLeftTime.setText(new SimpleDateFormat("'D'-d").format(new Date(DATE_FORMAT.parse(datum.getEndDate()).getTime() - new Date().getTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (datum.getAchievement() == 100) {
                holder.tvComplete.setVisibility(View.VISIBLE);
                holder.ivComplete.setVisibility(View.VISIBLE);
                holder.itemView.setEnabled(false);
            } else {
                holder.tvComplete.setVisibility(View.GONE);
                holder.ivComplete.setVisibility(View.GONE);
                holder.itemView.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        Log.i("Page", "transformPage: " + position);
        page.setTranslationX(position * - (2 * 20 * MainActivity.dpUnit + 30 * MainActivity.dpUnit));
        page.setTranslationY(Math.abs(position) * 20 * MainActivity.dpUnit);
    }

    public class ChallengeListViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivType;
        private TextView tvTitle;
        private TextView tvChallengesCnt;
        private ProgressBar pbCard;
        private TextView tvProgress;
        private TextView tvDate;
        private ImageView ivMore;
        private ImageView ivComplete;
        private TextView tvComplete;
        private TextView tvLeftTime;

        public ChallengeListViewHolder(@NonNull View itemView) {
            super(itemView);

            /* findViewByID */
            ivType = itemView.findViewById(R.id.iv_card_main);
            tvTitle = itemView.findViewById(R.id.tv_title_card_main);
            tvChallengesCnt = itemView.findViewById(R.id.tv_challengers_card_main);
            pbCard = itemView.findViewById(R.id.pb_card_main);
            tvProgress = itemView.findViewById(R.id.tv_progress_percentage_card_main);
            tvDate = itemView.findViewById(R.id.tv_start_date_card_main);
            ivMore = itemView.findViewById(R.id.iv_more_card_main);
            ivComplete = itemView.findViewById(R.id.iv_success_card_main);
            tvComplete = itemView.findViewById(R.id.tv_clear_card_main);
            tvLeftTime = itemView.findViewById(R.id.tv_lefttime_card_main);

            /* Set On Click Listener */
            ivMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu_popup_more, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_start:
                            Intent intent = new Intent(mContext, RecordActivity.class);
                            intent.putExtra("challenge", mData.get(getAdapterPosition()));
                            ((MainActivity) mContext).startActivityForResult(intent, MainActivity.REQUEST_CODE);
                            return true;
                        case R.id.menu_delete:
                            deleteItem(getAdapterPosition());
                            return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }
}
