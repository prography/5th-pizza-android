package com.prography.progrpahy_pizza.src.addChallenge.interfaces;

import android.view.View;
import android.widget.AdapterView;

public interface AddChallengeActivityView extends View.OnClickListener{
    void postvalidateSuccess();
    void postvalidateFailure();

    void onPickerItemSelected(int which, String selected);

    void onPickerPositiveClick();
}
