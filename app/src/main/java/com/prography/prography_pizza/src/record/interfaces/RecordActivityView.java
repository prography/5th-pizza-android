package com.prography.prography_pizza.src.record.interfaces;

import android.location.LocationListener;
import android.view.View;

public interface RecordActivityView extends View.OnClickListener, LocationListener {

    void validateSuccess(String message);
    void validateFailure(String message);
}
