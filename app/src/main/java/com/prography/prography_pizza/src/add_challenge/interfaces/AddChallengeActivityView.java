package com.prography.prography_pizza.src.add_challenge.interfaces;

import android.view.View;

import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;

public interface AddChallengeActivityView extends View.OnClickListener{
    void postvalidateSuccess(AddChallengeResponse.Data datum);
    void postvalidateFailure();

    void onPickerItemSelected(int which, String selected);

    void onPickerPositiveClick();
}
