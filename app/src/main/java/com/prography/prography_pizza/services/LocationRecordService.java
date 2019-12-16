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
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseService;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.RecordActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationRecordService extends Service implements LocationListener {
    public static Intent SERVICE = null;
    public static Context mContext;

    private double mGoal = 0;
    private int mGoalType = RecordActivity.GOALTYPE_DISTANCE;
    private float mGoalPercent = 0.f;

    private boolean SERVICE_RUNNING = false;
    private Thread timerThread;

    private LocationManager locationManager;
    private LocationDataSet locationDataSet = new LocationDataSet();

    private MainResponse.Data mChallenge = null;

    public static class LocationDataSet implements Serializable {
        private ArrayList<Location> locations = new ArrayList<>();

        public long startTime = 0;
        public long totalLastLeftTime = 0;
        public long leftTime = 0;
        public long totalTime = 0;

        public Location prevLocation = null;
        public Location curLocation = null;
        public float totalDistance = 0.0f;
        public float increaseDistance = 0.0f;
        public float velocity = 0.0f;
        public float velocityAvg = 0.0f;

        public int powerRed = 0;
        public int powerGreen = 0;
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
        locationDataSet = new LocationDataSet();
        mChallenge = (MainResponse.Data) intent.getSerializableExtra("challenge");
    }

    @SuppressLint("MissingPermission")
    public void runThread() {
        SERVICE_RUNNING = true;
        locationDataSet.startTime = System.currentTimeMillis(); // 현재 서비스 시작시간 고정.
        locationDataSet.totalLastLeftTime += locationDataSet.leftTime; // 가장 최근까지 누적 leftTime 더하기.
        // Thread
        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (SERVICE_RUNNING) {
                    locationDataSet.leftTime = System.currentTimeMillis() - locationDataSet.startTime; // 지속시간 누적
                    locationDataSet.totalTime = locationDataSet.totalLastLeftTime + locationDataSet.leftTime; // 총 시간 계산
                    if (locationDataSet.totalTime != 0 && locationDataSet.totalDistance != 0) {
                        locationDataSet.velocityAvg = (locationDataSet.totalDistance / locationDataSet.totalTime) * 3.6f;
                    } // 속도 계산

                    /* Send Intent Back to Activity */
                    Intent intentToActivity = new Intent("location-data");
                    intentToActivity.putExtra("locationDataSet", locationDataSet);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentToActivity);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        timerThread.start();

        // Location Thread
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
    }

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
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        SERVICE = null;
        stopThread();
    }

    public void initNotificationChannel() {
        Intent startIntent = new Intent(this, RecordActivity.class);
        startIntent.putExtra("challenge", mChallenge);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("운동중")
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
            locationDataSet.curLocation = location;

            locationDataSet.locations.add(location);
            if (locationDataSet.prevLocation != null) {
                locationDataSet.increaseDistance = location.distanceTo(locationDataSet.prevLocation);
            } else {
                locationDataSet.prevLocation = locationDataSet.curLocation;
            }
            locationDataSet.velocity = locationDataSet.increaseDistance * 3.6f; // km/h : 러닝 범위 5 km/h ~ 15 km/h

            if (locationDataSet.velocity >= 5.f && locationDataSet.velocity < 10.f) {
                locationDataSet.powerRed = 255;
                locationDataSet.powerGreen = 255 - (int) ((locationDataSet.velocity - 5.f) / 10 * 255);
                if (locationDataSet.powerGreen <= 0)
                    locationDataSet.powerGreen = 0;
                else if (locationDataSet.powerGreen >= 255)
                    locationDataSet.powerGreen = 255;
            } else if (locationDataSet.velocity >= 10.f && locationDataSet.velocity <= 15.f) {
                locationDataSet.powerRed = 255 - (int) ((locationDataSet.velocity - 10.f) / 10 * 255);
                if (locationDataSet.powerRed <= 0)
                    locationDataSet.powerRed = 0;
                else if (locationDataSet.powerRed >= 255)
                    locationDataSet.powerRed = 255;
                locationDataSet.powerGreen = 255;
            } else if (locationDataSet.velocity < 5.f) {
                locationDataSet.powerRed = 255;
                locationDataSet.powerGreen = 0;
            } else {
                locationDataSet.powerRed = 0;
                locationDataSet.powerGreen = 255;
            }
            locationDataSet.totalDistance += locationDataSet.increaseDistance;
        } else {
            // Provider가 Network일때는 연산하지 않음. -> Toast를 띄워줄까?
        }
        locationDataSet.prevLocation = locationDataSet.curLocation;
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
        return null;
    }
}
