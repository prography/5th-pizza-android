package com.prography.prography_pizza.src.main.interfaces;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.prography.prography_pizza.src.main.models.ChallengeResponse;

import java.util.ArrayList;


public interface MainActivityView extends View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    void validateSuccess(ArrayList<ChallengeResponse.Data> data);
    void validateFailure();

    void validateDeleteSuccess();
    void starteDeleteProcess(int challengeId);
}
