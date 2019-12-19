package com.prography.prography_pizza.config;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.prography.prography_pizza.src.ApplicationClass.X_ACCESS_TOKEN;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class XSocialTokenInterceptor implements Interceptor {

    private String token = null;
    private String tokenName = null;

    public XSocialTokenInterceptor(String tokenName, String token) {
        this.token = token;
        this.tokenName = tokenName;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();

        builder.addHeader(tokenName, token);
        return chain.proceed(builder.build());
    }
}
