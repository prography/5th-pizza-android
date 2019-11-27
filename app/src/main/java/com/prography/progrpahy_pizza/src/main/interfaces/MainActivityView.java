package com.prography.progrpahy_pizza.src.main.interfaces;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import java.util.ArrayList;

public interface MainActivityView extends View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    void validateSuccess(ArrayList<ChallengeResponse.Data> data);
    void validateFailure();

}
