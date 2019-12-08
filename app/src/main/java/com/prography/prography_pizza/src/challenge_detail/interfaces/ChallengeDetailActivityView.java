package com.prography.prography_pizza.src.challenge_detail.interfaces;

import android.view.View;

public interface ChallengeDetailActivityView extends View.OnClickListener {
    void validateSuccess();
    void validateFailure(int code);
}
