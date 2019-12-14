package com.prography.prography_pizza.src.record.interfaces;

import android.location.LocationListener;
import android.view.View;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.tabs.TabLayout;

public interface RecordActivityView extends View.OnClickListener, LocationListener, TabLayout.OnTabSelectedListener, OnMapReadyCallback {

    void validateSuccess(String message);
    void validateFailure(String message);
}
