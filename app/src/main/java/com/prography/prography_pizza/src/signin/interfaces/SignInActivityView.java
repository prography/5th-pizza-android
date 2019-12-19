package com.prography.prography_pizza.src.signin.interfaces;

import android.view.View;

public interface SignInActivityView extends View.OnClickListener {

    void validateSuccess(String token);

    void validateFailure();
}
