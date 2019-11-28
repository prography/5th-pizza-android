package com.prography.progrpahy_pizza.src.common.utils;

import com.prography.progrpahy_pizza.src.main.adapter.ChallengeListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
    private ChallengeListAdapter challengeListAdapter;

    public SwipeToDelete(ChallengeListAdapter challengeListAdapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.challengeListAdapter = challengeListAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        challengeListAdapter.deleteItem(position);
    }
}
