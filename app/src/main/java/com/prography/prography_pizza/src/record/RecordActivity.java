package com.prography.prography_pizza.src.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.GPSRecordService;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.adapter.RecordPagerAdapter;
import com.prography.prography_pizza.src.record.fragments.CurrentFragment;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
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
    private GoogleMap mvGoogleRecord;
    private PieChart pcRecord;
    private TabLayout tlRecord;
    private ViewPager vpRecord;

    private Handler progressHandler;
    private LocationManager locationManager;
    private AlertDialog mAlertDialog;
    private RecordPagerAdapter rpaRecord;

    private ArrayList<MyLocation> myLocations = new ArrayList<>();
    private ArrayList<MapPolyline> mapPolylines = new ArrayList<>();
    private CurrentFragment mCurrentFragment;
    private CurrentFragment mLeftFragment;

    private Intent serviceIntent;

    private double mGoal = 0;
    private int mGoalType = GOALTYPE_DISTANCE;
    private float mGoalPercent = 0.f;

    private boolean TIMER_RUNNING = false;
    private long startTime;
    private long totalLastLeftTime = 0;
    private long leftTime = 0;
    private long totalTime = 0;

    private Location prevLocation;
    private Location curLocation;
    private float totalDistance = 0.0f;
    private float increaseDistance = 0.0f;
    private float velocity = 0.0f;
    private float velocityAvg = 0.0f;

    private PieDataSet mPieDataSet;
    private PieData mPieData;

    public class MyLocation {
        double longitude;
        double latitude;
        double altitude;

        public MyLocation(double longitude, double latitude, double altitude) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.altitude = altitude;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fg_map_record);

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
        MainResponse.Data challenge = (MainResponse.Data) intent.getSerializableExtra("challenge");
        mGoal = challenge.getTime();
        switch (challenge.getObjectUnit()) {
            case "distance":
                mGoalType = GOALTYPE_DISTANCE;
                switch (challenge.getExerciseType()) {
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
                switch (challenge.getExerciseType()) {
                    case "running":
                        tvGoal.setText((int) mGoal + "분 달리기");
                        break;
                    case "cycling":
                        tvGoal.setText((int) mGoal + "분 자전거 타기");
                        break;
                }
                mGoal = mGoal * 60 * 1000; // min -> mills
        }

        /* Set BackGround Service */
        if (GPSRecordService.SERVICE == null) {
            serviceIntent = new Intent(this, GPSRecordService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = GPSRecordService.SERVICE;
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

        /* Get Location - GPS */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /* Set MapView */
        mvRecord.setZoomLevel(1, false);
        mSupportMapFragment.getMapAsync(this);

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
        List<PieEntry> pieEntryList = new ArrayList<>();
        PieEntry pieEntry1 = new PieEntry(0.0f);
        pieEntry1.setLabel("");
        pieEntryList.add(pieEntry1);
        PieEntry pieEntry2 = new PieEntry(0.1f);
        pieEntry2.setLabel("");
        pieEntryList.add(pieEntry2);
        mPieDataSet = new PieDataSet(pieEntryList, "");
        mPieDataSet.setColors(new int[] {R.color.piechart_record, R.color.transparent}, this);
        mPieDataSet.setLabel("");
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

    @SuppressLint("HandlerLeak")
    @Override
    protected void onStart() {
        super.onStart();
        /* Set Progress Handler */
        progressHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {

                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        mGoalPercent = (float) (totalDistance / mGoal) * 100;
                        break;
                    case GOALTYPE_TIME:
                        mGoalPercent = (float) (totalTime / mGoal) * 100;
                        break;
                }

                /* PieChart */
                List<PieEntry> pieEntryList = new ArrayList<>();
                PieEntry pieEntry1 = new PieEntry(mGoalPercent / 100);
                pieEntry1.setLabel("");
                pieEntryList.add(pieEntry1);
                PieEntry pieEntry2 = new PieEntry(1 - mGoalPercent / 100);
                pieEntry2.setLabel("");
                pieEntryList.add(pieEntry2);
                mPieDataSet.setValues(pieEntryList);
                mPieData.setDataSet(mPieDataSet);
                pcRecord.setData(mPieData);
                pcRecord.invalidate();

                /* Current Fragment */
                String distance = "";
                String distanceUnit = "";
                String goalDistance = "";
                String goalDistanceUnit = "";
                String goalTime = "";
                if (totalDistance < 1000) {
                    distance = String.format("%.1f", totalDistance);
                    distanceUnit = "m";
                } else {
                    distance = String.format("%.1f", totalDistance / 1000);
                    distanceUnit = "km";
                }
                Date date = new Date((long) msg.obj);

                int pace = 0;
                if (velocityAvg != 0)
                    pace = Math.round(60 / velocityAvg);

                Log.i("FragmentManagerList", getSupportFragmentManager().getFragments().toString());
                if (mLeftFragment == null) {
                    mLeftFragment = (CurrentFragment) getSupportFragmentManager().getFragments().get(1);
                }
                if (mCurrentFragment == null) {
                    mCurrentFragment = (CurrentFragment) getSupportFragmentManager().getFragments().get(2);
                }
                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        if (mGoal - totalDistance < 1000) {
                            goalDistanceUnit = "m";
                            goalDistance = String.format("%.1f", (float) mGoal - totalDistance);
                        } else {
                            goalDistanceUnit = "km";
                            goalDistance = String.format("%.1f", (float) (mGoal - totalDistance) / 1000);
                        }
                        mLeftFragment.setCurrentView(goalDistance, goalDistanceUnit,
                                "--",
                                String.format("%.1f", mGoalPercent));
                        break;
                    case GOALTYPE_TIME:
                        goalTime = String.valueOf((int) ((mGoal - totalTime) / 60 / 1000)); // mills -> mins
                        mLeftFragment.setCurrentView("-.-", "m",
                                goalTime,
                                String.format("%.1f", mGoalPercent));
                        break;
                }
                mCurrentFragment.setCurrentView(distance, distanceUnit,
                        CURRENT_TIME_FORMAT.format(date),
                        String.format("%02d'%02d''", pace / 60, pace % 60));
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceIntent != null) {
            stopService(GPSRecordService.SERVICE);
            serviceIntent = null;
        }
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
    public void onMapReady(GoogleMap googleMap) {
        mvGoogleRecord = googleMap;
         Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        mvGoogleRecord.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .bearing(location.getBearing())
                .build()));
        mvGoogleRecord.getUiSettings().setMyLocationButtonEnabled(true);
        mvGoogleRecord.setMyLocationEnabled(true);
        mvGoogleRecord.setIndoorEnabled(false);
        mvGoogleRecord.getUiSettings().setCompassEnabled(true);
        mvGoogleRecord.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            curLocation = location;
            double longitude = curLocation.getLongitude();
            double latitude = curLocation.getLatitude();
            double altitude = curLocation.getAltitude();

            /* Google Map */
            mvGoogleRecord.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .target(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()))
                    .zoom(17)
                    .bearing(location.getBearing())
                    .build()));

            myLocations.add(new MyLocation(longitude, latitude, altitude));
            if (prevLocation != null) {
                increaseDistance = curLocation.distanceTo(prevLocation);
            }
            velocity = increaseDistance * 3.6f; // km/h : 러닝 범위 5 km/h ~ 15 km/h

            int powerRed;
            int powerGreen;
            if (velocity >= 5.f && velocity < 10.f) {
                powerRed = 255;
                powerGreen = 255 - (int) ((velocity - 5.f) / 10 * 255);
                if (powerGreen <= 0)
                    powerGreen = 0;
                else if (powerGreen >= 255)
                    powerGreen = 255;
            } else if (velocity >= 10.f && velocity <= 15.f) {
                powerRed = 255 - (int) ((velocity - 10.f) / 10 * 255);
                if (powerRed <= 0)
                    powerRed = 0;
                else if (powerRed >= 255)
                    powerRed = 255;
                powerGreen = 255;
            } else if (velocity < 5.f) {
                powerRed = 255;
                powerGreen = 0;
            } else {
                powerRed = 0;
                powerGreen = 255;
            }
            totalDistance += increaseDistance;
            /* 새로운 polyline 점 추가 */
            if (prevLocation != null) {
                MapPolyline mapPolyline = new MapPolyline();
                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(prevLocation.getLatitude(), prevLocation.getLongitude()));
                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                mapPolyline.setLineColor(Color.argb(255, powerRed, powerGreen, 0)); // 색상 범위 : 빨간색 (255, 0, 0) ~ 노란색 (255, 255, 0) ~ 초록색 (0, 255, 0)
                prevLocation = curLocation;
                mvRecord.addPolyline(mapPolyline);
                mapPolylines.add(mapPolyline);
            }
        } else {

        }
        prevLocation = curLocation;
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

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start_record:
                if (!TIMER_RUNNING) {
                    ivStartRecord.setImageResource(R.drawable.ic_pause);
                    TIMER_RUNNING = true;
                    startTime = System.currentTimeMillis(); // 초기 시간 기록
                    // Timer Thread
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            totalLastLeftTime += leftTime; // 가장 최근 leftTime 더하기.
                            while (TIMER_RUNNING) {
                                Message message = new Message();
                                leftTime = System.currentTimeMillis() - startTime; // 지속시간 누적
                                totalTime = totalLastLeftTime + leftTime; // 총 시간 계산
                                if (totalTime != 0 && totalDistance != 0) {
                                    velocityAvg = (totalDistance / totalTime) * 3.6f;
                                } // 속도 구하기 (시간 종속)
                                message.obj = totalTime;
                                progressHandler.sendMessage(message);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    // Location Thread
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

                    ivSubmitRecord.setVisibility(View.INVISIBLE);
                } else {
                    TIMER_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);
                    ivSubmitRecord.setVisibility(View.VISIBLE);
                    locationManager.removeUpdates(this);
                }
                break;
            case R.id.iv_submit_record:
                new AlertDialog.Builder(this).setMessage("이대로 제출하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tryPostRecord(totalTime, (double) totalDistance);
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
                break;
        }
    }
}
