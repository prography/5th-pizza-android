package com.prography.prography_pizza.src.record.interfaces;

import android.view.View;

import com.google.android.material.tabs.TabLayout;

public interface RecordActivityView extends View.OnClickListener, TabLayout.OnTabSelectedListener {

    void validateSuccess(String message);

    void validateFailure(String message);
}
