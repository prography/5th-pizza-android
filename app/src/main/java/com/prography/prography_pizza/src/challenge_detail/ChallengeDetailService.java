package com.prography.prography_pizza.src.challenge_detail;

import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailActivityView;
import com.prography.prography_pizza.src.challenge_detail.interfaces.ChallengeDetailRetrofitInterface;
import com.prography.prography_pizza.src.challenge_detail.models.ChallengeDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class ChallengeDetailService {
    final ChallengeDetailActivityView mChallengeDetailActivityView;

    public ChallengeDetailService(ChallengeDetailActivityView mChallengeDetailActivityView) {
        this.mChallengeDetailActivityView = mChallengeDetailActivityView;
    }

    public void getDetail(int cid) {
        final ChallengeDetailRetrofitInterface challengeDetailRetrofitInterface = getRetrofit().create(ChallengeDetailRetrofitInterface.class);
        challengeDetailRetrofitInterface.getChallengeDetail(cid).enqueue(new Callback<ChallengeDetailResponse>(){

            @Override
            public void onResponse(Call<ChallengeDetailResponse> call, Response<ChallengeDetailResponse> response) {
                final ChallengeDetailResponse challengeDetailResponse=response.body();
                if(challengeDetailResponse==null){
                    mChallengeDetailActivityView.validateFailure();
                    return;
                }

                mChallengeDetailActivityView.validateSuccess(challengeDetailResponse.getData());
            }

            @Override
            public void onFailure(Call<ChallengeDetailResponse> call, Throwable t) {
                mChallengeDetailActivityView.validateFailure();
            }
        });
    }

}
