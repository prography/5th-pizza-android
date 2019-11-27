package com.prography.progrpahy_pizza.src.splash.interfaces;

import com.prography.progrpahy_pizza.src.splash.models.SplashResponse;

public interface SplashActivityView {

    void validateSuccess(SplashResponse.Data data, String token);

    void validateFailure(String message);
}
