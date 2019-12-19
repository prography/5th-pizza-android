package com.prography.prography_pizza.src.challenge_detail.interfaces;

import android.view.View;

import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import java.util.ArrayList;

public interface ChallengeDetailActivityView extends View.OnClickListener {
    void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data);

    void validateFailure();
}
