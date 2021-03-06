package com.prography.prography_pizza.src;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.prography.prography_pizza.config.XAccessTokenInterceptor;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationClass extends Application {

    private static volatile ApplicationClass instance = null;

    public static ApplicationClass getApplicationClassContext() {
        if (instance == null)
            throw new IllegalStateException("Error of ApplicationClass");
        return instance;
    }

    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=uft-8");
    public static MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    // 테스트 서버 주소
    //public static String BASE_URL = "http://pizza-dev.ap-northeast-2.elasticbeanstalk.com/";
    // 실서버 주소
    public static String BASE_URL = "https://pizza.geusan.com";

    public static String BASE_FIREBASE_STORAGE = "gs://prography.appspot.com";

    public static SharedPreferences sSharedPreferences = null;

    // SharedPreferences 키 값
    public static String TAG = "PIZZA-PROGRPAHY";

    // JWT Token 값
    public static String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";

    // LOGIN Type 값
    public static String LOGIN_TYPE = "LOGIN-TYPE";
    public static final String TYPE_GOOGLE = "google";
    public static final String TYPE_NAVER = "naver";
    public static final String TYPE_FACEBOOK = "facebook";
    public static final String TYPE_KAKAO = "kakao";

    // USER INFO 값
    public static String USER_PROFILE = "USER-PROFILE";
    public static String USER_NAME = "USER-NAME";
    public static String USER_EMAIL = "USER-EMAIL";

    // 튜토리얼 진행도 값
    public static String TUTORIALED_MAIN = "TUTORIALED_MAIN";
    public static String TUTORIALED_ADD = "TUTORIALED_ADD";
    public static String TUTORIALED_DETAIL = "TUTORIALED_DETAIL";
    public static String TUTORIALED_RECORD = "TUTORIALED_RECORD";
    public static String TUTORIALED_MYPAGE = "TUTORIALED_MYPAGE";

    //날짜 형식
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);
    public static SimpleDateFormat CURRENT_TIME_FORMAT = new SimpleDateFormat("mm:ss", Locale.getDefault());

    // Retrofit 인스턴스
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (sSharedPreferences == null) {
            sSharedPreferences = getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        }

        /* Kakao SDK Init*/
        KakaoSDK.init(new KakaoSDKAdapter());

        /* Facebook SDK Init */
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        CURRENT_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    private static class KakaoSDKAdapter extends KakaoAdapter {


        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return ApplicationClass::getApplicationClassContext;
        }
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addNetworkInterceptor(new XAccessTokenInterceptor()) // JWT 자동 헤더 전송
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) //데이터 파싱
                    .build();
        }

        return retrofit;
    }
}
