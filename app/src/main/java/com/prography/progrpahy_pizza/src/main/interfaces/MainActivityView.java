package com.prography.progrpahy_pizza.src.main.interfaces;

import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import java.util.ArrayList;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface MainActivityView extends View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    void getvalidateSuccess(ArrayList<ChallengeResponse.Data> data);
    void getvalidateFailure();

}
