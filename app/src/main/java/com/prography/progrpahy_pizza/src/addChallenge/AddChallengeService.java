package com.prography.progrpahy_pizza.src.addChallenge;

import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeActivityView;
import com.prography.progrpahy_pizza.src.addChallenge.interfaces.AddChallengeRetrofitInterface;
import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeRequest;
import com.prography.progrpahy_pizza.src.addChallenge.models.AddChallengeResponse;
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
        addChallengeRetrofitInterface.postChallenge(new AddChallengeRequest(routineType, time, objectUnit, exerciseType)).enqueue(new Callback<AddChallengeResponse>() {
            @Override
            public void onResponse(Call<AddChallengeResponse> call, Response<AddChallengeResponse> response) {
                final AddChallengeResponse addChallengeResponse = response.body();
               if (addChallengeResponse == null) {
                    addChallengeActivityView.postvalidateFailure();
                    return;
                    // Fail
                }
                // Success
                addChallengeActivityView.postvalidateSuccess(addChallengeResponse.getDatum());
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
