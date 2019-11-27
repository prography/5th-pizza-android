package com.prography.progrpahy_pizza.src.common.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//recyclerView item 간격 조정
public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public RecyclerViewDecoration(int divHeight){
        this.divHeight=divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top=divHeight;
    }
}
