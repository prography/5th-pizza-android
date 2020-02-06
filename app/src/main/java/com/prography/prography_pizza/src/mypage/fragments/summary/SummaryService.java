package com.prography.prography_pizza.src.mypage.fragments.summary;

import com.prography.prography_pizza.src.mypage.fragments.summary.interfaces.SummaryFragmentView;
import com.prography.prography_pizza.src.mypage.fragments.summary.interfaces.SummaryRetrofitInterface;
import com.prography.prography_pizza.src.mypage.fragments.summary.models.BadgeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class SummaryService {
    final SummaryFragmentView mSummaryFragmentView;

    public SummaryService(SummaryFragmentView mSummaryFragmentView) {
        this.mSummaryFragmentView = mSummaryFragmentView;
    }

    public void tryGetBadges() {
        final SummaryRetrofitInterface summaryRetrofitInterface = getRetrofit().create(SummaryRetrofitInterface.class);
        summaryRetrofitInterface.getBadges().enqueue(new Callback<BadgeResponse>() {
            @Override
            public void onResponse(Call<BadgeResponse> call, Response<BadgeResponse> response) {
                BadgeResponse badgeResponse = response.body();
                if (badgeResponse == null) {
                    mSummaryFragmentView.validateFailure();
                    return;
                }
                mSummaryFragmentView.validateSuccess(badgeResponse.getData());
            }

            @Override
            public void onFailure(Call<BadgeResponse> call, Throwable t) {
                t.printStackTrace();
                mSummaryFragmentView.validateFailure();
            }
        });
    }
}
