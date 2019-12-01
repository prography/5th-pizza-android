package com.prography.prography_pizza.src.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.models.ChallengeResponse;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import static com.prography.prography_pizza.src.ApplicationClass.TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    private Toolbar tbRecord;
    private ActionBar abRecord;
    private TextView tvGoal;
    private TextView tvProgress;
    private TextView tvProgressMap;
    private TextView tvProgressUnit;
    private TextView tvProgressMapUnit;
    private ImageView ivStartRecord;
    private ImageView ivSubmitRecord;
    private ImageView ivChangeRecord;
    private ImageView ivLocation;
    private MapView mvRecord;
    private PieChart pcRecord;
    private ImageView ivForeground;

    private Handler progressHandler;
    private LocationManager locationManager;
    private AlertDialog mAlertDialog;

    private ArrayList<MyLocation> myLocations = new ArrayList<>();
    private ArrayList<MapPolyline> mapPolylines = new ArrayList<>();

    private double mGoal = 0;
    private int mGoalType = 1;
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
    private boolean MODE_MAP = false;

    private PieDataSet mPieDataSet;
    private PieData mPieData;

    private State mState = new State();



    public class State {
        public static final int MODE_PACE = 10;
        public static final int MODE_PROGRESS = 11;
        public static final int MODE_DISTANCE = 12;
        public static final int MODE_TIME = 13;
        public static final int MODE_VELOCITY = 14;

        private int currentState = MODE_DISTANCE;

        public void setCurrentState(int currentState) {
            this.currentState = currentState;
        }

        public int getCurrentState() {
            return currentState;
        }
    }

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

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        /* findViewByID */
        tvGoal = findViewById(R.id.tv_goal_record);
        tvProgress = findViewById(R.id.tv_cur_progress_record);
        tvProgressUnit = findViewById(R.id.tv_cur_progress_unit_record);
        tvProgressMap = findViewById(R.id.tv_cur_progress_map_record);
        tvProgressMapUnit = findViewById(R.id.tv_cur_progress_unit_map_record);
        ivStartRecord = findViewById(R.id.iv_start_record);
        ivSubmitRecord = findViewById(R.id.iv_submit_record);
        ivChangeRecord = findViewById(R.id.iv_change_record);
        mvRecord = findViewById(R.id.frame_mapview_record);
        pcRecord = findViewById(R.id.pc_record);
        ivLocation = findViewById(R.id.iv_location_record);
        ivForeground = findViewById(R.id.iv_foreground_record);
        tbRecord = findViewById(R.id.toolbar_record);

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
        ChallengeResponse.Data challenge = (ChallengeResponse.Data) intent.getSerializableExtra("challenge");
        mGoal = challenge.getTime();
        switch (challenge.getObjectUnit()) {
            case "distance":
                mGoalType = 1;
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
                mGoalType = 2;
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

        /* Toolbar */
        setSupportActionBar(tbRecord);
        abRecord = getSupportActionBar();
        abRecord.setDisplayShowTitleEnabled(false);
        abRecord.setDisplayHomeAsUpEnabled(true);
        abRecord.setHomeAsUpIndicator(R.drawable.ic_close);

        /* Get Location - GPS */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /* Set MapView */
        mvRecord.setZoomLevel(1, false);

        /* Set Progress Handler */
        progressHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (mState.getCurrentState()) {
                    case State.MODE_DISTANCE:
                        if (totalDistance < 1000) {
                            tvProgressMap.setText(String.format("%.1f", totalDistance)); // < 1000 : 0.0 m
                            tvProgress.setText(String.format("%.1f", totalDistance)); // < 1000 : 0.0 m
                            tvProgressUnit.setText("m");
                            tvProgressMapUnit.setText("m");
                        }
                        else {
                            tvProgressMap.setText(String.format("%.2f", totalDistance / 1000)); // >= 1000 : 0.00 km
                            tvProgress.setText(String.format("%.2f", totalDistance / 1000)); // >= 1000 : 0.00 km
                            tvProgressUnit.setText("km");
                            tvProgressMapUnit.setText("km");
                        }
                        break;
                    case State.MODE_PACE:
                        int pace = 0;
                        if (velocity != 0)
                            pace = Math.round(60 / velocity);
                        tvProgress.setText(String.format("%02d'%02d''", pace / 60, pace % 60));
                        tvProgressMap.setText(String.format("%02d'%02d''", pace / 60, pace % 60));
                        tvProgressUnit.setText("pace");
                        tvProgressMapUnit.setText("pace");
                        break;
                    case State.MODE_PROGRESS:
                        tvProgress.setText(String.format("%.1f", mGoalPercent));
                        tvProgressMap.setText(String.format("%.1f", mGoalPercent));
                        tvProgressUnit.setText("%");
                        tvProgressMapUnit.setText("%");
                        break;
                    case State.MODE_TIME:
                        Date date = new Date((long) msg.obj);
                        tvProgress.setText(TIME_FORMAT.format(date));
                        tvProgressMap.setText(TIME_FORMAT.format(date));
                        tvProgressUnit.setText("sec");
                        tvProgressMapUnit.setText("sec");
                        break;
                    case State.MODE_VELOCITY:
                        tvProgress.setText(String.format("%.2f", velocity));
                        tvProgressUnit.setText("km/s .avg");
                        tvProgressMap.setText(String.format("%.2f", velocity));
                        tvProgressMapUnit.setText("km/s .avg");
                        break;
                }
                switch (mGoalType) {
                    case 1:
                        mGoalPercent = (float) (totalDistance / mGoal) * 100;
                        break;
                    case 2:
                        mGoalPercent = (float) (totalTime / mGoal) * 100;
                        break;
                }

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
            }
        };

        /* Set on Click Listener */
        ivStartRecord.setOnClickListener(this);
        ivSubmitRecord.setOnClickListener(this);
        mvRecord.setOnClickListener(this);
        ivChangeRecord.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        tvProgressMapUnit.setOnClickListener(this);
        tvProgressMap.setOnClickListener(this);
        tvProgressUnit.setOnClickListener(this);
        tvProgress.setOnClickListener(this);


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
        ivForeground.setVisibility(View.VISIBLE);


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            curLocation = location;
            double longitude = curLocation.getLongitude();
            double latitude = curLocation.getLatitude();
            double altitude = curLocation.getAltitude();
            myLocations.add(new MyLocation(longitude, latitude, altitude));
            increaseDistance = curLocation.distanceTo(prevLocation);
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
            if (totalTime != 0 && totalDistance != 0) {
                velocityAvg = (totalDistance / totalTime) * 3.6f;
            }
            /* 새로운 polyline 점 추가*/
            MapPolyline mapPolyline = new MapPolyline();
            mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(prevLocation.getLatitude(), prevLocation.getLongitude()));
            mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
            mapPolyline.setLineColor(Color.argb(255, powerRed, powerGreen, 0)); // 색상 범위 : 빨간색 (255, 0, 0) ~ 노란색 (255, 255, 0) ~ 초록색 (0, 255, 0)
            prevLocation = curLocation;
            mvRecord.addPolyline(mapPolyline);
            mapPolylines.add(mapPolyline);
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
                                totalTime = totalLastLeftTime + leftTime;
                                message.obj = totalTime;
                                progressHandler.sendMessage(message);
                                try {
                                    Thread.sleep(100);
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
                    ivChangeRecord.setVisibility(View.VISIBLE);
                } else {
                    TIMER_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);
                    ivChangeRecord.setVisibility(View.INVISIBLE);
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

            case R.id.iv_location_record:
                if (MODE_MAP) {
                    ivForeground.setVisibility(View.VISIBLE);
                    tvProgress.setVisibility(View.VISIBLE);
                    tvProgressUnit.setVisibility(View.VISIBLE);
                    tvProgressMap.setVisibility(View.GONE);
                    tvProgressMapUnit.setVisibility(View.GONE);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
                } else {
                    ivForeground.setVisibility(View.GONE);
                    tvProgress.setVisibility(View.GONE);
                    tvProgressUnit.setVisibility(View.GONE);
                    tvProgressMap.setVisibility(View.VISIBLE);
                    tvProgressMapUnit.setVisibility(View.VISIBLE);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
                }
                MODE_MAP = !MODE_MAP;
                break;
            case R.id.iv_change_record:
            case R.id.tv_cur_progress_map_record:
            case R.id.tv_cur_progress_unit_map_record:
            case R.id.tv_cur_progress_unit_record:
            case R.id.tv_cur_progress_record:
                // 모드 변경
                switch (mState.getCurrentState()) {
                    case State.MODE_DISTANCE:
                        mState.setCurrentState(State.MODE_PACE);
                        break;
                    case State.MODE_PACE:
                        mState.setCurrentState(State.MODE_PROGRESS);
                        break;
                    case State.MODE_PROGRESS:
                        mState.setCurrentState(State.MODE_TIME);
                        break;
                    case State.MODE_TIME:
                        mState.setCurrentState(State.MODE_VELOCITY);
                        break;
                    case State.MODE_VELOCITY:
                        mState.setCurrentState(State.MODE_DISTANCE);
                        break;
                }
                break;
        }
    }
}
