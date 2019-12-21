package com.prography.prography_pizza.src.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.LocationRecordService;
import com.prography.prography_pizza.services.models.LocationDataSet;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.adapter.RecordPagerAdapter;
import com.prography.prography_pizza.src.record.fragments.CurrentFragment;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    public static final int GOALTYPE_DISTANCE = 1;
    public static final int GOALTYPE_TIME = 2;

    private Toolbar tbRecord;
    private ActionBar abRecord;
    private TextView tvGoal;
    private ImageView ivStartRecord;
    private ImageView ivSubmitRecord;
    private MapView mvRecord;
    private PieChart pcRecord;
    private TabLayout tlRecord;
    private ViewPager vpRecord;

    private AlertDialog mAlertDialog;
    private RecordPagerAdapter rpaRecord;

    private ArrayList<MapPolyline> mapPolylines = new ArrayList<>();
    private CurrentFragment mCurrentFragment;
    private CurrentFragment mLeftFragment;

    private Intent serviceIntent;
    private LocationRecordService mLocationRecordService;
    private ServiceConnection mConnection;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private MainResponse.Data mChallenge;
    private double mGoal = 0;
    private int mGoalType = GOALTYPE_DISTANCE;
    private float mGoalPercent = 0.f;
    private LocationDataSet mLocationDataSet = null;

    private PieDataSet mPieDataSet;
    private PieData mPieData;
    private boolean SERVICE_RUNNING = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        /* findViewByID */
        tvGoal = findViewById(R.id.tv_goal_record);
        ivStartRecord = findViewById(R.id.iv_start_record);
        ivSubmitRecord = findViewById(R.id.iv_submit_record);
        mvRecord = findViewById(R.id.mv_record);
        pcRecord = findViewById(R.id.pc_record);
        tbRecord = findViewById(R.id.toolbar_record);
        tlRecord = findViewById(R.id.tl_record);
        vpRecord = findViewById(R.id.vp_record);

        /* Permission Listener */
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        /* Set MapView */
                        mvRecord.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading); // 권한 설정 필요
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        showToast("permission Denied\n" + deniedPermissions.toString());
                    }
                })
                .setDeniedMessage("권한 승인을 해야만 이용이 가능합니다.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        /* Get Intent */
        Intent intent = getIntent();
        mChallenge = intent.getParcelableExtra("challenge");
        mGoal = mChallenge.getTime();
        switch (mChallenge.getObjectUnit()) {
            case "distance":
                mGoalType = GOALTYPE_DISTANCE;
                switch (mChallenge.getExerciseType()) {
                    case "running":
                        tvGoal.setText((int) mGoal + "km 달리기");
                        break;
                    case "cycling":
                        tvGoal.setText((int) mGoal + "km 자전거 타기");
                        break;
                }
                mGoal = mGoal * 1000; // km -> m
                break;
            case "time":
                mGoalType = GOALTYPE_TIME;
                switch (mChallenge.getExerciseType()) {
                    case "running":
                        tvGoal.setText((int) mGoal + "분 달리기");
                        break;
                    case "cycling":
                        tvGoal.setText((int) mGoal + "분 자전거 타기");
                        break;
                }
                mGoal = mGoal * 60 * 1000; // min -> mills
        }
        /* 액티비티 재시작일경우 기존 location 데이터로부터 polyline 다시 그리기 */
        if (intent.getParcelableExtra("locationDataSetInit") != null) {
            mLocationDataSet = intent.getParcelableExtra("locationDataSetInit");
            for (int i = 0; i < mLocationDataSet.locations.size() - 1; i++) {
                MapPolyline mapPolyline = new MapPolyline();
                Location prev = mLocationDataSet.locations.get(i);
                Location cur = mLocationDataSet.locations.get(i + 1);
                ArrayList<Integer> colors = mLocationDataSet.powerColors.get(i);

                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(prev.getLatitude(), prev.getLongitude()));
                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(cur.getLatitude(), cur.getLongitude()));
                mapPolyline.setLineColor(Color.argb(255, colors.get(0), colors.get(1), 0)); // 색상 범위 : 빨간색 (255, 0, 0) ~ 노란색 (255, 255, 0) ~ 초록색 (0, 255, 0)
                mvRecord.addPolyline(mapPolyline);
                mapPolylines.add(mapPolyline);
            }
        }

        /* Toolbar */
        setSupportActionBar(tbRecord);
        abRecord = getSupportActionBar();
        abRecord.setDisplayShowTitleEnabled(false);
        abRecord.setDisplayHomeAsUpEnabled(true);
        abRecord.setHomeAsUpIndicator(R.drawable.ic_back);

        /* TabLayout */
        tlRecord.addOnTabSelectedListener(this);

        /* ViewPager */
        rpaRecord = new RecordPagerAdapter(getSupportFragmentManager(), mGoalType, mGoal);
        vpRecord.setAdapter(rpaRecord);
        vpRecord.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlRecord));

        /* Get Location Provider Client */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /* Set MapView */
        mvRecord.setZoomLevel(1, false);

        /* Set BackGround Service Receiver */
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationDataSetReceiver, new IntentFilter("location-data"));
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mLocationRecordService = ((LocationRecordService.LocationBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        /* Set on Click Listener */
        ivStartRecord.setOnClickListener(this);
        ivSubmitRecord.setOnClickListener(this);

        /* Make AlertDialog */
        mAlertDialog = new AlertDialog.Builder(this).setMessage("운동을 저장하지 않고 종료하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

        /* Init View */
        /* PieChart */
        List<PieEntry> pieEntryList = new ArrayList<>(Arrays.asList(
                new PieEntry(0.0f, ""),
                new PieEntry(0.1f, "")));
        mPieDataSet = new PieDataSet(pieEntryList, "");
        mPieDataSet.setColors(new int[]{R.color.piechart_record, R.color.transparent}, this);
        mPieDataSet.setDrawValues(false);

        mPieData = new PieData(mPieDataSet);
        pcRecord.setData(mPieData);
        pcRecord.invalidate();
        pcRecord.setHoleColor(Color.TRANSPARENT);
        pcRecord.setHoleRadius(90);
        pcRecord.setDrawEntryLabels(false);
        pcRecord.getLegend().setEnabled(false);
        pcRecord.getDescription().setEnabled(false);
        pcRecord.setTouchEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceIntent != null) {
            stopService(serviceIntent);
            unbindService(mConnection);
            serviceIntent = null;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocationDataSetReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mAlertDialog.show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mAlertDialog.show();
    }

    private void tryPostRecord(double totalTime, double totalDistance) {
        showProgressDialog();
        RecordService recordService = new RecordService(this);
        recordService.postRecord(totalTime, totalDistance);
    }

    @Override
    public void validateSuccess(String message) {
        hideProgressDialog();
        showToast("업로드에 성공하였습니다.");
        finish();
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        showToast("업로드에 실패하였습니다.");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        vpRecord.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start_record:
                if (!SERVICE_RUNNING) {
                    // 운동 시작
                    SERVICE_RUNNING = true;
                    ivStartRecord.setImageResource(R.drawable.ic_pause);
                    ivSubmitRecord.setVisibility(View.INVISIBLE);

                    // 서비스 시작
                    if (mLocationRecordService == null) {
                        // 초기 시작
                        serviceIntent = new Intent(this, LocationRecordService.class);
                        serviceIntent.putExtra("challenge", mChallenge);
                        serviceIntent.putExtra("locationDataSet", mLocationDataSet);
                        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }
                    } else {
                        // 재시작
                        mLocationRecordService.runThread();
                    }

                } else {
                    // 운동 정지
                    SERVICE_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);
                    ivSubmitRecord.setVisibility(View.VISIBLE);

                    // 서비스 종료
                    mLocationRecordService.stopThread();
                }
                break;
            case R.id.iv_submit_record:
                // 1. 목표를 달성했을 때.
                // 2. 충분한 거리를 달렸지만 목표를 달성하지 못했을 때.
                new AlertDialog.Builder(this).setMessage("목표를 아직 달성하지 못했습니다.\n여기까지만 저장하고 잠시 쉴까요?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryPostRecord(mLocationDataSet.totalTime, (double) mLocationDataSet.totalDistance);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create().show();
                // 3. 달린 거리나 시간이 너무 부족할 때,
                break;
        }
    }


    private BroadcastReceiver mLocationDataSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLocationDataSet = intent.getParcelableExtra("locationDataSet");

            /* Set View */
            if (mLocationDataSet != null) {
                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        mGoalPercent = (float) (mLocationDataSet.totalDistance / mGoal) * 100;
                        break;
                    case GOALTYPE_TIME:
                        mGoalPercent = (float) (mLocationDataSet.totalTime / mGoal) * 100;
                        break;
                }

                /* 새로운 polyline 점 추가 */
                int lastIndex = mLocationDataSet.locations.size() - 1;
                if (lastIndex > 0) {
                    // location에 저장된 데이터셋이 2개 이상일 때만 직선 그림.
                    Location prev = mLocationDataSet.locations.get(lastIndex - 1);
                    Location cur = mLocationDataSet.locations.get(lastIndex);
                    ArrayList<Integer> curPower = mLocationDataSet.powerColors.get(mLocationDataSet.powerColors.size() - 1);

                    MapPolyline mapPolyline = new MapPolyline();
                    mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(prev.getLatitude(), prev.getLongitude()));
                    mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(cur.getLatitude(), cur.getLongitude()));
                    mapPolyline.setLineColor(Color.argb(255, curPower.get(0), curPower.get(1), 0)); // 색상 범위 : 빨간색 (255, 0, 0) ~ 노란색 (255, 255, 0) ~ 초록색 (0, 255, 0)
                    mvRecord.addPolyline(mapPolyline);
                    mapPolylines.add(mapPolyline);

                    /* TestOnly */
                    /* Debug Text View */
                    ((TextView) findViewById(R.id.tv_debug))
                            .setText("Provider: " + cur.getProvider() + "\n"
                                    + "PrevLocation: " + prev.getLatitude() + ", " + prev.getLongitude() + "\n"
                                    + "CurLocation: " + cur.getLatitude() + ", " + cur.getLongitude() + "\n"
                                    + "Color: " + curPower.get(0) + ", " + curPower.get(1) + ", 0" + "\n"
                                    + "Velocity: " + cur.getSpeed() + " m/s\n"
                                    + "Accuracy: " + cur.getAccuracy() + "\n"
                                    + "DistanceTo: " + cur.distanceTo(prev) + " m\n"
                                    + "Distance(Vel): " + cur.getSpeed() * 1 + "m");
                }

                /* Current Fragment */
                String distance = "";
                String distanceUnit = "";
                String goalDistance = "";
                String goalDistanceUnit = "";
                String goalTime = "";
                if (mLocationDataSet.totalDistance < 1000) {
                    distance = String.format("%.1f", mLocationDataSet.totalDistance);
                    distanceUnit = "m";
                } else {
                    distance = String.format("%.1f", mLocationDataSet.totalDistance / 1000);
                    distanceUnit = "km";
                }
                Date date = new Date(mLocationDataSet.totalTime);

                int pace = 0;
                if (mLocationDataSet.velocityAvg != 0)
                    pace = Math.round(1 / mLocationDataSet.velocityAvg); // m/s -> sec/meter

                if (mLeftFragment == null) {
                    mLeftFragment = (CurrentFragment) getSupportFragmentManager().getFragments().get(1);
                }
                if (mCurrentFragment == null) {
                    mCurrentFragment = (CurrentFragment) getSupportFragmentManager().getFragments().get(2);
                }
                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        if (mGoal - mLocationDataSet.totalDistance < 1000) {
                            goalDistanceUnit = "m";
                            goalDistance = String.format("%.1f", (float) mGoal - mLocationDataSet.totalDistance);
                        } else {
                            goalDistanceUnit = "km";
                            goalDistance = String.format("%.1f", (float) (mGoal - mLocationDataSet.totalDistance) / 1000);
                        }
                        mLeftFragment.setCurrentView(goalDistance, goalDistanceUnit,
                                "--",
                                String.format("%.1f", mGoalPercent));
                        break;
                    case GOALTYPE_TIME:
                        goalTime = String.valueOf((int) ((mGoal - mLocationDataSet.totalTime) / 60 / 1000)); // mills -> mins
                        mLeftFragment.setCurrentView("-.-", "m",
                                goalTime,
                                String.format("%.1f", mGoalPercent));
                        break;
                }
                mCurrentFragment.setCurrentView(distance, distanceUnit,
                        CURRENT_TIME_FORMAT.format(date),
                        String.format("%02d'%02d''", pace / 60, pace % 60));
            }

            /* PieChart */
            List<PieEntry> pieEntryList = new ArrayList<>(Arrays.asList(
                    new PieEntry(mGoalPercent / 100, ""),
                    new PieEntry(1 - mGoalPercent / 100, "")));
            mPieDataSet.setValues(pieEntryList);
            mPieData.setDataSet(mPieDataSet);
            pcRecord.setData(mPieData);
            pcRecord.invalidate();
        }
    };
}
