package com.prography.prography_pizza.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.prography.prography_pizza.BuildConfig;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.interfaces.LocationRecordServiceView;
import com.prography.prography_pizza.services.models.LocationDataSet;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.RecordActivity;

public class LocationRecordService extends Service implements LocationRecordServiceView {
    public static final int DEFAULT_PAUSETIME = 2;

    // Unit : km/h
    public static final float MINIMUM_SPEED = 1.f;
    public static final float MAXIMUM_SPEED = 36.f;
    public static final float SPEED_RANGE0 = 5.f;
    public static final float SPEED_RANGE1 = 10.f;
    public static final float SPEED_RANGE2 = 15.f;

    // Unit : mils
    public static final int MINIMUM_INTERVAL_TIME = 1000;

    // Unit : m
    public static final float MINIMUM_INTERVAL_DISTANCE = 2.f;

    public static Intent sSERVICE = null;
    public static Context sContext;
    private final IBinder mBinder = new LocationBinder();
    private boolean SERVICE_RUNNING = false;
    private int COUNT_PAUSE_TIME_IN_SEC = 3;
    private Thread timerThread;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LocationDataSet mLocationDataSet;
    private MainResponse.Data mChallenge;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sSERVICE = intent;

        initData(intent);
        initNotificationChannel();
        runThread();

        return START_STICKY;
    }

    public void initData(Intent intent) {
        if (intent != null) {
            if (intent.getParcelableExtra("locationDataSet") == null) {
                mLocationDataSet = new LocationDataSet();
            } else {
                mLocationDataSet = intent.getParcelableExtra("locationDataSet");
            }
            mChallenge = intent.getParcelableExtra("challenge");
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void runThread() {
        SERVICE_RUNNING = true;
        // Thread
        timerThread = new Thread(() -> {
            while (SERVICE_RUNNING) {
                if (COUNT_PAUSE_TIME_IN_SEC > 0) {
                    COUNT_PAUSE_TIME_IN_SEC--;
                    mLocationDataSet.totalTime += 1; // 총 시간 계산 (매 쓰레드마다 1s 씩 증가
                    if (mLocationDataSet.totalTime != 0 && mLocationDataSet.totalDistance != 0) {
                        mLocationDataSet.speedAvg = (mLocationDataSet.totalDistance / mLocationDataSet.totalTime); // m/s
                    } else {
                        mLocationDataSet.speedAvg = 0.f;
                    }
                    // 평균 속도 계산

                    /* Send Intent Back to Activity */
                    Intent intentToActivity = new Intent("location-data");
                    intentToActivity.putExtra("locationDataSet", mLocationDataSet);
                    LocalBroadcastManager.getInstance(sContext).sendBroadcast(intentToActivity);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.start();

        // Location Thread
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void stopThread() {
        SERVICE_RUNNING = false;
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

        /* Fused Location Provider Init */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /* Location Request Init */
        mLocationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(MINIMUM_INTERVAL_TIME)
                .setSmallestDisplacement(MINIMUM_INTERVAL_DISTANCE)
                .setFastestInterval(MINIMUM_INTERVAL_TIME);

        /* Location Callback Init */
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                FitnessOptions fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_READ)
                        .build();

                Location lastLocation = locationResult.getLastLocation();
                if (lastLocation != null) {
                    /* Release Only */
                    if (!BuildConfig.DEBUG) {
                        /* Speed가 지정한 속력 범위가 아닐 때는 아무것도 안하고 return */
                        if (lastLocation.getSpeed() * 3.6f < MINIMUM_SPEED || lastLocation.getSpeed() * 3.6f > MAXIMUM_SPEED) {
                            return;
                        }
                        /* 위치 제공자가 "fused"일 때만 연산. 이외의 모의 위치 거부 */
                        if (!lastLocation.getProvider().equals("fused")) {
                            return;
                        }
                    }
                    COUNT_PAUSE_TIME_IN_SEC = DEFAULT_PAUSETIME; // PauseTime 초기화

                    mLocationDataSet.locations.add(new LatLng(lastLocation));
                    mLocationDataSet.speeds.add(lastLocation.getSpeed() * 3.6f);
                    mLocationDataSet.increaseDistance = getDistanceDx(lastLocation, mLocationDataSet.locations.size() > 1 ?
                            mLocationDataSet.locations.get(mLocationDataSet.locations.size() - 2) : null);
                    mLocationDataSet.totalDistance += mLocationDataSet.increaseDistance;
                }
            }
        };

    }

    public float getDistanceDx(@NonNull Location cur, @Nullable LatLng prev) {
        float result;
        if (prev != null) {
            if (cur.getSpeed() * 3.6f < SPEED_RANGE0) {
                result = cur.getSpeed(); // Speed 활용
            } else {
                result = (float) new LatLng(cur).distanceTo(prev); // DistanceTo 활용
            }
        } else {
            result = 0.f;
        }
        return result;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        sSERVICE = null;
        stopThread();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sSERVICE = null;
    }

    public void initNotificationChannel() {
        Intent startIntent = new Intent(this, RecordActivity.class);
        startIntent.putExtra("challenge", mChallenge);
        startIntent.putExtra("locationDataSetInit", mLocationDataSet);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("지금 열심히 운동중!")
                .setContentText("GPS를 수집중에 있습니다.")
                .setOngoing(true)
                .setWhen(0)
                .setShowWhen(false)
                .setContentIntent(PendingIntent.getActivity(this, 0, startIntent, 0));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("1", "prography", NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1, notificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public class LocationBinder extends Binder {
        public LocationRecordService getService() {
            return LocationRecordService.this;
        }
    }
}
