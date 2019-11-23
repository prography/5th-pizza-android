package com.prography.progrpahy_pizza.src.addChallenge;

import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeRetrofitInterface;
import com.prography.progrpahy_pizza.src.addChallenge.models.ChallengeRequest;
import com.prography.progrpahy_pizza.src.main.models.ChallengeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.progrpahy_pizza.src.ApplicationClass.getRetrofit;

public class AddChallengeService {
    final AddChallengeActivityView addChallengeActivityView;

    public AddChallengeService(AddChallengeActivityView addChallengeActivityView) {
        this.addChallengeActivityView = addChallengeActivityView;
    }

    public void postChallenge(String routineType, double time, String objectUnit, String exerciseType ){
        final AddChallengeRetrofitInterface addChallengeRetrofitInterface=getRetrofit().create(AddChallengeRetrofitInterface.class);
        addChallengeRetrofitInterface.postChallenge(new ChallengeRequest(routineType, time, objectUnit, exerciseType)).enqueue(new Callback<ChallengeResponse>() {
            @Override
            public void onResponse(Call<ChallengeResponse> call, Response<ChallengeResponse> response) {
                final ChallengeResponse challengeResponse = response.body();
                if (challengeResponse == null) {
                    addChallengeActivityView.postvalidateFailure();
                    return;
                    // Fail
                }
                // Success
                addChallengeActivityView.postvalidateSuccess();
            }

            @Override
            public void onFailure(Call<ChallengeResponse> call, Throwable t) {
                t.printStackTrace();
                addChallengeActivityView.postvalidateFailure();
                // Fail
            }
        });
    }
}
