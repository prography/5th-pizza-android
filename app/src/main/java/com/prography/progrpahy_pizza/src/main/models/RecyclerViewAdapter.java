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
    private ArrayList<String> challengeResponses;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(ArrayList<String> challengeResponses, Context context) {
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

        holder.challenge_list.setText(challengeResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return challengeResponses.size();
    }

    public void addItem(String s){
        challengeResponses.add(s);
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView challenge_list;


        ViewHolder(final View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
           challenge_list=itemView.findViewById(R.id.tv_title_item_challenge_main);

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
