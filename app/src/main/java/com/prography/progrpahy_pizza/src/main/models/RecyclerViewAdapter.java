package com.prography.progrpahy_pizza.src.main.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.record.RecordActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Challenge> challenges;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(ArrayList<Challenge> challenges, Context context) {
        this.challenges = challenges;
        layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Challenge challenge=challenges.get(position);

        if(challenge!=null) {
            holder.routineType.setText(challenge.getRoutineType());
            holder.time.setText(String.valueOf(challenge.getTime()));
            holder.objectUnit.setText(challenge.getObjectUnit());
            holder.exerciseType.setText(challenge.getExerciseType());
        }

       /* holder.routineType.setText(challenges.get(position));
        holder.time.setText(challenges.get(position));
        holder.objectUnit.setText(challenges.get(position));
        holder.exerciseType.setText(challenges.get(position));*/

    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public void addItem(Challenge challenge){
        challenges.add(challenge);
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView routineType;
        TextView time;
        TextView objectUnit;
        TextView exerciseType;


        ViewHolder(final View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            routineType = itemView.findViewById(R.id.list_routineType);
            time = itemView.findViewById(R.id.list_time);
            objectUnit = itemView.findViewById(R.id.list_objectUnit);
            exerciseType = itemView.findViewById(R.id.list_exerciseType);

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
