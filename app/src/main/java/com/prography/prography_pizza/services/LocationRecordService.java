package com.prography.prography_pizza.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.interfaces.LocationRecordServiceView;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.RecordActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationRecordService extends Service implements LocationRecordServiceView {
    public static Intent SERVICE = null;
    public static Context mContext;

    private double mGoal = 0;
    private int mGoalType = RecordActivity.GOALTYPE_DISTANCE;
    private float mGoalPercent = 0.f;

    private boolean SERVICE_RUNNING = false;
    private int COUNT_PAUSE_TIME_IN_SEC = 3;

    public boolean isSERVICE_RUNNING() {
        return SERVICE_RUNNING;
    }

    public void setSERVICE_RUNNING(boolean SERVICE_RUNNING) {
        this.SERVICE_RUNNING = SERVICE_RUNNING;
    }

    private Thread timerThread;

    private LocationManager locationManager;
    private LocationDataSet mLocationDataSet = null;

    private MainResponse.Data mChallenge = null;

    public static class LocationDataSet implements Serializable {
        public ArrayList<Location> locations = new ArrayList<>();
        public Location prevLocation = null;
        public Location curLocation = null;
        public float totalDistance = 0.0f;
        public float increaseDistance = 0.0f;
        public float velocity = 0.0f;
        public float velocityAvg = 0.0f;

        public long startTime = 0;
        public long totalLastLeftTime = 0;
        public long leftTime = 0;
        public long totalTime = 0;

        public ArrayList<ArrayList<Integer>> powerColors = new ArrayList<>();
        public int curPowerRed = 0;
        public int curPowerGreen = 0;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SERVICE = intent;

        initData(intent);
        initNotificationChannel();
        runThread();

        return START_STICKY;
    }

    public void initData(Intent intent) {
        if (intent.getSerializableExtra("locationDataSet") == null) {
            mLocationDataSet = new LocationDataSet();
        } else {
            mLocationDataSet = (LocationDataSet) intent.getSerializableExtra("locationDataSet");
        }
        mChallenge = (MainResponse.Data) intent.getSerializableExtra("challenge");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void runThread() {
        SERVICE_RUNNING = true;
        mLocationDataSet.startTime = System.currentTimeMillis(); // 현재 서비스 시작시간 고정.
        mLocationDataSet.totalLastLeftTime += mLocationDataSet.leftTime; // 가장 최근까지 누적 leftTime 더하기.
        // Thread
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (SERVICE_RUNNING) {
                    if (COUNT_PAUSE_TIME_IN_SEC > 0) {
                        if (COUNT_PAUSE_TIME_IN_SEC == 3) {
                            // 재시작시 첫 구간
                            mLocationDataSet.startTime = System.currentTimeMillis(); // 현재 서비스 시작시간 고정.
                            mLocationDataSet.totalLastLeftTime += mLocationDataSet.leftTime; // 가장 최근까지 누적 leftTime 더하기.
                        }
                        COUNT_PAUSE_TIME_IN_SEC--;
                        mLocationDataSet.leftTime = System.currentTimeMillis() - mLocationDataSet.startTime; // 지속시간 누적
                        mLocationDataSet.totalTime = mLocationDataSet.totalLastLeftTime + mLocationDataSet.leftTime; // 총 시간 계산
                        if (mLocationDataSet.totalTime != 0 && mLocationDataSet.totalDistance != 0) {
                            mLocationDataSet.velocityAvg = (mLocationDataSet.totalDistance / mLocationDataSet.totalTime) * 3.6f;
                        } // 속도 계산

                        /* Send Intent Back to Activity */
                        Intent intentToActivity = new Intent("location-data");
                        intentToActivity.putExtra("locationDataSet", mLocationDataSet);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentToActivity);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        });
        timerThread.start();

        // Location Thread
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
    }

    @Override
    public void stopThread() {
        SERVICE_RUNNING = false;
        locationManager.removeUpdates(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SERVICE = null;
        stopThread();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        SERVICE = null;
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

    @Override
    public void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            COUNT_PAUSE_TIME_IN_SEC = 3; // PauseTime 초기화
            mLocationDataSet.curLocation = location;
            mLocationDataSet.locations.add(location);
            if (mLocationDataSet.prevLocation != null) {
                mLocationDataSet.increaseDistance = location.distanceTo(mLocationDataSet.prevLocation);
            } else {
                mLocationDataSet.prevLocation = mLocationDataSet.curLocation;
            }
            mLocationDataSet.velocity = mLocationDataSet.increaseDistance * 3.6f; // km/h : 러닝 범위 5 km/h ~ 15 km/h

            if (mLocationDataSet.velocity >= 5.f && mLocationDataSet.velocity < 10.f) {
                mLocationDataSet.curPowerRed = 255;
                mLocationDataSet.curPowerGreen = 255 - (int) ((mLocationDataSet.velocity - 5.f) / 10 * 255);
                if (mLocationDataSet.curPowerGreen <= 0)
                    mLocationDataSet.curPowerGreen = 0;
                else if (mLocationDataSet.curPowerGreen >= 255)
                    mLocationDataSet.curPowerGreen = 255;
            } else if (mLocationDataSet.velocity >= 10.f && mLocationDataSet.velocity <= 15.f) {
                mLocationDataSet.curPowerRed = 255 - (int) ((mLocationDataSet.velocity - 10.f) / 10 * 255);
                if (mLocationDataSet.curPowerRed <= 0)
                    mLocationDataSet.curPowerRed = 0;
                else if (mLocationDataSet.curPowerRed >= 255)
                    mLocationDataSet.curPowerRed = 255;
                mLocationDataSet.curPowerGreen = 255;
            } else if (mLocationDataSet.velocity < 5.f) {
                mLocationDataSet.curPowerRed = 255;
                mLocationDataSet.curPowerGreen = 0;
            } else {
                mLocationDataSet.curPowerRed = 0;
                mLocationDataSet.curPowerGreen = 255;
            }
            ArrayList<Integer> powers = new ArrayList<>();
            powers.add(mLocationDataSet.curPowerRed);
            powers.add(mLocationDataSet.curPowerGreen);
            mLocationDataSet.powerColors.add(powers);
            mLocationDataSet.totalDistance += mLocationDataSet.increaseDistance;
        } else {
            // Provider가 Network일때는 연산하지 않음. -> Toast를 띄워줄까?
        }
        mLocationDataSet.prevLocation = mLocationDataSet.curLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocationBinder();
    public class LocationBinder extends Binder {
        public LocationRecordService getService() {
            return LocationRecordService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
