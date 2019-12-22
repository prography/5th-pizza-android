package com.prography.prography_pizza.config;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class XSocialTokenInterceptor implements Interceptor {

    private String token;

    public XSocialTokenInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();

        builder.addHeader("x-social-token", token);
        return chain.proceed(builder.build());
    }
}
