package com.prography.progrpahy_pizza.src.main.interfaces;

import android.view.View;

import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import java.util.ArrayList;

public interface MainActivityView extends View.OnClickListener{

    void getvalidateSuccess(ArrayList<ChallengeResponse.Data> data);
    void getvalidateFailure();

}
