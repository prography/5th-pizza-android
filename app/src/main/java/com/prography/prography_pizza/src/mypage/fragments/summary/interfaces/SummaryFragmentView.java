package com.prography.prography_pizza.src.mypage.fragments.summary.interfaces;

import com.prography.prography_pizza.src.challenge_detail.models.RankResponse;
import com.prography.prography_pizza.src.mypage.fragments.summary.models.BadgeResponse;

import java.util.ArrayList;

public interface SummaryFragmentView {

    void validateSuccess(ArrayList<BadgeResponse.Data> badges);
    void validateFailure();
}
