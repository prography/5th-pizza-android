package com.prography.progrpahy_pizza.src.record;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.record.interfaces.RecordActivityView;

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

import static com.prography.progrpahy_pizza.src.ApplicationClass.TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    private Toolbar tbRecord;
    private ActionBar abRecord;
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

    // TODO: Progressbar 만들기.

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

        /* Get Intent */

        /* Toolbar */
        setSupportActionBar(tbRecord);
        abRecord = getSupportActionBar();
        abRecord.setDisplayShowTitleEnabled(false);
        abRecord.setDisplayHomeAsUpEnabled(true);
        abRecord.setHomeAsUpIndicator(R.drawable.ic_close);

        /* Get Location - GPS */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /* Set MapView */
        mvRecord.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading); // 권한 설정 필요
        mvRecord.setZoomLevel(1, false);

        /* Set Timer Handler */
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
                            tvProgressMap.setText(String.format("%.2f", totalDistance / 1000) + " km"); // >= 1000 : 0.00 km
                            tvProgress.setText(String.format("%.2f", totalDistance / 1000) + " km"); // >= 1000 : 0.00 km
                            tvProgressUnit.setText("km");
                            tvProgressMapUnit.setText("km");
                        }
                        break;
                    case State.MODE_PACE:
                        int pace = Math.round(60 / velocity);
                        tvProgress.setText(String.format("%02d'%02d''", pace / 60, pace % 60));
                        tvProgressMap.setText(String.format("%02d'%02d''", pace / 60, pace % 60));
                        tvProgressUnit.setText("pace");
                        tvProgressMapUnit.setText("pace");
                        break;
                    case State.MODE_PROGRESS:
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
        PieEntry pieEntry1 = new PieEntry(0.4f);
        pieEntry1.setLabel("");
        pieEntryList.add(pieEntry1);
        PieEntry pieEntry2 = new PieEntry(0.6f);
        pieEntry2.setLabel("");
        pieEntryList.add(pieEntry2);
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
        pieDataSet.setColors(new int[] {R.color.piechart_record, R.color.transparent}, this);
        pieDataSet.setLabel("");
        pieDataSet.setDrawValues(false);
        PieData pieData = new PieData(pieDataSet);
        pcRecord.setData(pieData);
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
                    new Thread(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {
                            prevLocation = (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ) ?
                                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            while (TIMER_RUNNING) {
                                curLocation  = (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null ) ?
                                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) : locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                double longitude = curLocation.getLongitude();
                                double latitude = curLocation.getLatitude();
                                double altitude = curLocation.getAltitude();
                                myLocations.add(new MyLocation(longitude, latitude, altitude));
                                increaseDistance = curLocation.distanceTo(prevLocation);
                                velocity = increaseDistance * 3.6f; // km/h : 러닝 범위 5 km/h ~ 15 km/h
                                int power = (int) ((velocity - 5.0f) / 10 * 255);
                                if (power <= 0)
                                    power = 0;
                                else if (power >= 255)
                                    power = 255;

                                totalDistance += increaseDistance;
                                velocityAvg = (totalDistance / totalTime) * 3.6f;
                                /* 새로운 polyline 점 추가*/
                                MapPolyline mapPolyline = new MapPolyline();
                                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(prevLocation.getLatitude(), prevLocation.getLongitude()));
                                mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                                mapPolyline.setLineColor(Color.argb(255, 255 - power, power, 0));
                                prevLocation = curLocation;
                                mvRecord.addPolyline(mapPolyline);
                                mapPolylines.add(mapPolyline);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.i("LOCATION", "Longitude: " + longitude + ", Latitude: " + latitude + ", Altitude: " + altitude + ", Total Distance: " + totalDistance);
                            }
                        }
                    }).start();

                    ivSubmitRecord.setVisibility(View.INVISIBLE);
                    ivChangeRecord.setVisibility(View.VISIBLE);
                } else {
                    TIMER_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);
                    ivChangeRecord.setVisibility(View.INVISIBLE);
                    ivSubmitRecord.setVisibility(View.VISIBLE);
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
