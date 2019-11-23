package com.prography.progrpahy_pizza.src.addChallenge.interfaces;

import android.widget.AdapterView;

public interface AddChallengeActivityView extends AdapterView.OnItemSelectedListener{
    void postvalidateSuccess();
    void postvalidateFailure();
}
