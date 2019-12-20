package com.prography.prography_pizza.src.add_challenge;

import com.prography.prography_pizza.src.add_challenge.interfaces.AddChallengeActivityView;
import com.prography.prography_pizza.src.add_challenge.interfaces.AddChallengeRetrofitInterface;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeParams;
import com.prography.prography_pizza.src.add_challenge.models.AddChallengeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.prography.prography_pizza.src.ApplicationClass.getRetrofit;

public class AddChallengeService {
    final AddChallengeActivityView addChallengeActivityView;

    public AddChallengeService(AddChallengeActivityView addChallengeActivityView) {
        this.addChallengeActivityView = addChallengeActivityView;
    }

    public void postChallenge(String routineType, double time, String objectUnit, String exerciseType) {
        final AddChallengeRetrofitInterface addChallengeRetrofitInterface = getRetrofit().create(AddChallengeRetrofitInterface.class);
        addChallengeRetrofitInterface.postChallenge(new AddChallengeParams(routineType, time, objectUnit, exerciseType)).enqueue(new Callback<AddChallengeResponse>() {
            @Override
            public void onResponse(Call<AddChallengeResponse> call, Response<AddChallengeResponse> response) {
                final AddChallengeResponse addChallengeResponse = response.body();
                if (addChallengeResponse == null) {
                    addChallengeActivityView.postvalidateFailure();
                    return;
                    // Fail
                }
                // Success
                addChallengeActivityView.postvalidateSuccess(addChallengeResponse.getData().get(0));
            }

            @Override
            public void onFailure(Call<AddChallengeResponse> call, Throwable t) {
                t.printStackTrace();
                addChallengeActivityView.postvalidateFailure();
                // Fail
            }
        });
    }
}
