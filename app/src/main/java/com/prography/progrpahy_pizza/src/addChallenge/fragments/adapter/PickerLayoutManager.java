package com.prography.progrpahy_pizza.src.addChallenge.fragments.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.prography.progrpahy_pizza.src.addChallenge.fragments.interfaces.SelectorBotomSheetFragmentView;

public class PickerLayoutManager extends LinearLayoutManager {
    SelectorBotomSheetFragmentView mSelectorBotomSheetFragmentView = null;
    private int mIndex;
    private RecyclerView recyclerView;
    public PickerLayoutManager(Context context) {
        super(context);
    }

    public PickerLayoutManager(Context context, int orientation, boolean reverseLayout, SelectorBotomSheetFragmentView mSelectorBotomSheetFragmentView, int recyclerIndex) {
        super(context, orientation, reverseLayout);
        this.mSelectorBotomSheetFragmentView = mSelectorBotomSheetFragmentView;
        mIndex = recyclerIndex;
    }

    private void updateChildrenAlpha() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float bottom = (float) getDecoratedBottom(child);
            float top = (float) getDecoratedTop(child);
            float childCenter = bottom + (top - bottom) / 2f;
            float center = getHeight() / 2f;
            child.setAlpha((center - Math.abs(center - childCenter)) / center);// alpha : 0.0 ~ 1.0
            child.setScaleX((center - 0.5f * Math.abs(center - childCenter)) / center); // scale : 0.5 ~ 1.0
            child.setScaleY((center - 0.5f * Math.abs(center - childCenter)) / center); // scale : 0.5 ~ 1.0
        }
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        new LinearSnapHelper().attachToRecyclerView(view);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateChildrenAlpha();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        updateChildrenAlpha();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int position = -1;
            float minDistance = (float) getHeight();

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float bottom = (float) getDecoratedBottom(child);
                float top = (float) getDecoratedTop(child);
                float childCenter = bottom + (top - bottom) / 2f;
                float distance = Math.abs(childCenter - getHeight() / 2f);
                Log.i("POSITION", "onScrollStateChanged: " + i + " : " + top + " : " + bottom + " : " + childCenter + " : " + distance);
                if (distance < minDistance) {
                    minDistance = distance;
                    position = recyclerView.getChildLayoutPosition(child);
                }
            }
            Log.i("POSITION", "onScrollStateChanged: " + position);
            mSelectorBotomSheetFragmentView.onItemSelected(mIndex, position);
        }
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            private static final float MILLISECONDS_PER_INCH = 100f;

            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return PickerLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
