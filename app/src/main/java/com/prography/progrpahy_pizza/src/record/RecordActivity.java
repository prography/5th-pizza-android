package com.prography.progrpahy_pizza.src.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.prography.progrpahy_pizza.R;
import com.prography.progrpahy_pizza.src.BaseActivity;
import com.prography.progrpahy_pizza.src.record.interfaces.RecordActivityView;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Date;

import static com.prography.progrpahy_pizza.src.ApplicationClass.TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    private TextView tvCountTime;
    private TextView tvDistance;
    private Button btnStartRecord;
    private Button btnSubmitRecord;
    private Button btnChangeRecord;
    private MapView mvRecord;
    private ImageView ivClose;

    private Handler timerHandler;
    private Handler distanceHandler;
    private LocationManager locationManager;

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

    private boolean MODE_VELOCITY = false;



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
        tvCountTime = findViewById(R.id.tv_cur_time_record);
        tvDistance = findViewById(R.id.tv_cur_distance_record);
        btnStartRecord = findViewById(R.id.btn_start_record);
        btnSubmitRecord = findViewById(R.id.btn_submit_record);
        btnChangeRecord = findViewById(R.id.btn_change_record);
        mvRecord = findViewById(R.id.frame_mapview_record);
        //ivClose = findViewById(R.id.iv_back_record);



        /* Get Location - GPS */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        /* Set MapView */
        mvRecord.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading); // 권한 설정 필요
        mvRecord.setZoomLevel(1, false);

        /* Set Timer Handler */
        timerHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (MODE_VELOCITY) {
                    tvCountTime.setText(velocityAvg + " km/s");
                } else {
                    Date date = new Date((long) msg.obj);
                    tvCountTime.setText(TIME_FORMAT.format(date));
                }
            }
        };
        distanceHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (MODE_VELOCITY) {
                    tvDistance.setText(velocity + " km/s");
                } else {
                    if (totalDistance < 1000)
                        tvDistance.setText(String.format("%.1f", totalDistance) + " m"); // < 1000 : 0.0 m
                    else
                        tvDistance.setText(String.format("%.2f", totalDistance / 1000) + " km"); // >= 1000 : 0.00 km
                }

            }
        };

        /* Set on Click Listener */
        btnStartRecord.setOnClickListener(this);
        btnSubmitRecord.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        mvRecord.setOnClickListener(this);
        btnChangeRecord.setOnClickListener(this);

        /* Init View */


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
            case R.id.btn_start_record:
                if (!TIMER_RUNNING) {
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
                                timerHandler.sendMessage(message);
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
                            prevLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            while (TIMER_RUNNING) {
                                curLocation  = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
                                distanceHandler.sendEmptyMessage(0);
                                Log.i("LOCATION", "Longitude: " + longitude + ", Latitude: " + latitude + ", Altitude: " + altitude + ", Total Distance: " + totalDistance);
                            }
                        }
                    }).start();

                    btnStartRecord.setText("일시정지");
                    btnSubmitRecord.setVisibility(View.INVISIBLE);
                    btnChangeRecord.setVisibility(View.VISIBLE);
                } else {

                    TIMER_RUNNING = false;
                    btnStartRecord.setText("계속");
                    btnChangeRecord.setVisibility(View.INVISIBLE);
                    btnSubmitRecord.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_submit_record:
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
            case R.id.btn_change_record:
                if (MODE_VELOCITY)
                    btnChangeRecord.setText("거리");
                else
                    btnChangeRecord.setText("속도");
                MODE_VELOCITY = !MODE_VELOCITY;
                break;
        }
    }
}
