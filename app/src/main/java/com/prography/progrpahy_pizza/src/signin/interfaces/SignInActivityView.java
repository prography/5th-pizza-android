package com.prography.progrpahy_pizza.src.signin.interfaces;

import android.view.View;

public interface SignInActivityView extends View.OnClickListener{

    void validateKakaoSuccess(String token);

    void validateKakaoFailure();
}
