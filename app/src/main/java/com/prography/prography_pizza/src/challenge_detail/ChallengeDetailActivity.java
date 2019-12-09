package com.prography.prography_pizza.src.challenge_detail;

import android.content.Intent;
import android.os.Bundle;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;

public class ChallengeDetailActivity extends BaseActivity {

    private int mChallengeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);


        /* findViewByID */


        /* Get Intent */
        Intent intent = getIntent();
        mChallengeId = intent.getIntExtra("challengeId", 0);

        /* Init View */
    }
}
