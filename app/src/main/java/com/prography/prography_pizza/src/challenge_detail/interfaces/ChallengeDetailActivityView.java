package com.prography.prography_pizza.src.challenge_detail.interfaces;

import android.view.View;

import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;
import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;

import java.util.ArrayList;

public interface ChallengeDetailActivityView extends View.OnClickListener {
    void validateSuccess(ArrayList<ChallengeDetailResponse.Data> data);

    void validateFailure();

    void validateRankSuccess(ArrayList<RankResponse.Data.Rank> ranks);
}
