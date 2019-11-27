package com.prography.progrpahy_pizza.src.addChallenge.interfaces;

import android.view.View;
import android.widget.AdapterView;

import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeResponse;

public interface AddChallengeActivityView extends View.OnClickListener{
    void postvalidateSuccess(AddChallengeResponse.Data datum);
    void postvalidateFailure();

    void onPickerItemSelected(int which, String selected);

    void onPickerPositiveClick();
}
