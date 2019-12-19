package com.prography.prography_pizza.src.add_challenge.fragments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.add_challenge.fragments.interfaces.SelectorBotomSheetFragmentView;

import java.util.ArrayList;

public class SelectorListAdapter extends RecyclerView.Adapter<SelectorListAdapter.SelectorViewHolder> {

    private ArrayList<String> selectorLists;
    private LayoutInflater layoutInflater;
    final SelectorBotomSheetFragmentView mSelectorBotomSheetFragmentView;
    private int recyclerIndex;

    public SelectorListAdapter(ArrayList<String> selectorLists, Context context, SelectorBotomSheetFragmentView selectorBotomSheetFragmentView, int recyclerIndex) {
        this.selectorLists = selectorLists;
        layoutInflater = LayoutInflater.from(context);
        mSelectorBotomSheetFragmentView = selectorBotomSheetFragmentView;
        this.recyclerIndex = recyclerIndex;
    }

    @NonNull
    @Override
    public SelectorViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_selectorbottomsheet, parent, false);

        return new SelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorViewHolder holder, final int position) {
        String item = selectorLists.get(position);
        if (item != null) {
            holder.tvItem.setText(item);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectorBotomSheetFragmentView.onItemClick(recyclerIndex, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return selectorLists.size();
    }

    public class SelectorViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public SelectorViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tv_item_selectorbottomsheet);

        }
    }
}
