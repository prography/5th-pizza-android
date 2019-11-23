package com.prography.progrpahy_pizza.src.record.interfaces;

import android.view.View;

public interface RecordActivityView extends View.OnClickListener {

    void validateSuccess(String message);
    void validateFailure(String message);
}
