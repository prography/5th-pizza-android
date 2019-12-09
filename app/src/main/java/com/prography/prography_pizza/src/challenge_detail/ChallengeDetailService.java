package com.prography.prography_pizza.src.challenge_detail;

import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailRetrofitInterface;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class ChallengeDetailService {
    final ChallengeDetailActivityView mChallengeDetailActivityView;

    public ChallengeDetailService(ChallengeDetailActivityView mChallengeDetailActivityView) {
        this.mChallengeDetailActivityView = mChallengeDetailActivityView;
    }

    public void getDetail(int cid) {
        final ChallengeDetailRetrofitInterface challengeDetailRetrofitInterface = getRetrofit().create(ChallengeDetailRetrofitInterface.class);
        challengeDetailRetrofitInterface.getChallengeDetail(cid);
    }
}
