package com.prography.prography_pizza.src.record;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.transition.Slide;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.prography.prography_pizza.BuildConfig;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.LocationRecordService;
import com.prography.prography_pizza.services.models.LocationDataSet;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    public static final int GOALTYPE_DISTANCE = 1;
    public static final int GOALTYPE_TIME = 2;
    public static float dpUnit;

    private Toolbar tbRecord;
    private ActionBar abRecord;
    private TextView tvGoal;
    private ImageView ivStartRecord;
    private TextView tvSubmitRecord;
    private com.mapbox.mapboxsdk.maps.MapView mvRecord;
    private PieChart pcRecord;
    private ConstraintLayout clBottomUpperContainer;
    private TextView tvProgress;
    private TextView tvProgressUnit;
    private TextView tvProgressDesc;
    private ImageView ivStar1;
    private ImageView ivStar2;
    private ImageView ivStar3;
    private TextView tvDistance;
    private TextView tvDistanceUnit;
    private TextView tvTime;
    private TextView tvPace;
    private TableLayout tblRecord;

    private AlertDialog mAlertDialog;

    private ArrayList<Point> mPoints = new ArrayList<>();

    private Intent serviceIntent;
    private LocationRecordService mLocationRecordService;
    private MapboxMap mvImplRecord;
    private Style mvStyle;
    private PermissionsManager permissionsManager;

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
        /* Get Mapbox SDK (Before set content)*/
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
        setContentView(R.layout.activity_record);

        /* findViewByID */
        tvGoal = findViewById(R.id.tv_goal_record);
        ivStartRecord = findViewById(R.id.iv_start_record);
        tvSubmitRecord = findViewById(R.id.tv_submit_record);
        mvRecord = findViewById(R.id.mv_record);
        pcRecord = findViewById(R.id.pc_record);
        tbRecord = findViewById(R.id.toolbar_record);
        clBottomUpperContainer = findViewById(R.id.cl_bottom_upper_container_record);
        tvProgress = findViewById(R.id.tv_progress_record);
        tvProgressUnit = findViewById(R.id.tv_progress_unit_record);
        tvProgressDesc = findViewById(R.id.tv_progress_desc_record);
        ivStar1 = findViewById(R.id.iv_star1_record);
        ivStar2 = findViewById(R.id.iv_star2_record);
        ivStar3 = findViewById(R.id.iv_star3_record);
        tvDistance = findViewById(R.id.tv_distance_record);
        tvDistanceUnit = findViewById(R.id.tv_distance_unit_record);
        tvTime = findViewById(R.id.tv_time_record);
        tvPace = findViewById(R.id.tv_pace_record);
        tblRecord = findViewById(R.id.tbl_record);

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


        /* Set Constants */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dpUnit = displayMetrics.densityDpi / (float) 160;

        /* Toolbar */
        setSupportActionBar(tbRecord);
        abRecord = getSupportActionBar();
        abRecord.setDisplayShowTitleEnabled(false);
        abRecord.setDisplayHomeAsUpEnabled(true);
        abRecord.setHomeAsUpIndicator(R.drawable.ic_back);

        /* Set MapView */
        mvRecord.onCreate(savedInstanceState);
        mvRecord.getMapAsync(this);

        /* Set BackGround Service Receiver */
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocationDataSetReceiver, new IntentFilter("location-data"));

        /* Set on Click Listener */
        ivStartRecord.setOnClickListener(this);
        tvSubmitRecord.setOnClickListener(this);

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
        clBottomUpperContainer.setTranslationY(100 * dpUnit);
        tblRecord.setTranslationY(100 * dpUnit);
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
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mvImplRecord = mapboxMap;
        mapboxMap.setStyle(Style.DARK, this);
        ivStartRecord.setEnabled(true);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        // Map is set.
        mvStyle = style;
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Init
            LocationComponent locationComponent = mvImplRecord.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            locationComponent.setRenderMode(RenderMode.GPS);
            UiSettings uiSettings = mvImplRecord.getUiSettings();
            uiSettings.setLogoGravity(Gravity.RIGHT | Gravity.TOP);
            uiSettings.setCompassEnabled(true);
            uiSettings.setCompassGravity(Gravity.TOP | Gravity.LEFT);
            LocalizationPlugin localizationPlugin = new LocalizationPlugin(mvRecord, mvImplRecord, style);
            try {
                localizationPlugin.matchMapLanguageWithDeviceDefault();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            /* Set Style */
            style.addSource(new GeoJsonSource("path", FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(mPoints))), new GeoJsonOptions().withLineMetrics(true)));
            style.addLayer(new LineLayer("exercise", "path").withProperties(
                    lineCap(Property.LINE_CAP_ROUND),
                    lineJoin(Property.LINE_JOIN_ROUND),
                    lineWidth(5 * dpUnit)));


            /* 액티비티 재시작일경우 기존 location 데이터로부터 Gradient Line 다시 그리기 */
            Intent intent = getIntent();
            if (intent.getParcelableExtra("locationDataSetInit") != null) {
                mLocationDataSet = intent.getParcelableExtra("locationDataSetInit");

                /* Set Line */
                mPoints.clear();
                for (LatLng latLng : mLocationDataSet.locations) {
                    mPoints.add(Point.fromLngLat(latLng.getLongitude(),latLng.getLatitude(),latLng.getAltitude()));
                }
                FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(mPoints)));

                /* Set Gradient Stop Points*/
                ArrayList<Expression.Stop> stops = new ArrayList<>();
                for (Integer key : mLocationDataSet.changePowerIdxs.keySet()) {
                    Expression expression = mLocationDataSet.changePowerIdxs.get(key);
                    float percentage;
                    if (mLocationDataSet.locations.size() != 0) {
                        percentage = key / (float) mLocationDataSet.locations.size();
                    } else {
                        percentage = 0.f;
                    }
                    if (expression != null) {
                        stops.add(Expression.stop(percentage, expression));
                    }
                }

                /* Set Style */
                GeoJsonSource path = style.getSourceAs("path");
                if (path != null) {
                    path.setGeoJson(featureCollection);
                }
                LineLayer lines = style.getLayerAs("exercise");
                if (lines != null) {
                    lines.setProperties(lineCap(Property.LINE_CAP_ROUND),
                            lineJoin(Property.LINE_JOIN_ROUND),
                            lineWidth(5 * dpUnit),
                            lineGradient(Expression.interpolate(Expression.linear(), Expression.lineProgress(),
                                    (Expression.Stop[]) stops.toArray())));
                }
            }

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mLocationRecordService = ((LocationRecordService.LocationBinder) service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvRecord.onDestroy();

        if (serviceIntent != null) {
            stopService(serviceIntent);
            unbindService(this);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start_record:
                if (!SERVICE_RUNNING) {
                    // 운동 시작
                    SERVICE_RUNNING = true;
                    ivStartRecord.setImageResource(R.drawable.ic_pause);

                    // 서비스 시작
                    if (mLocationRecordService == null) {
                        // 초기 시작
                        serviceIntent = new Intent(this, LocationRecordService.class);
                        serviceIntent.putExtra("challenge", mChallenge);
                        serviceIntent.putExtra("locationDataSet", mLocationDataSet);
                        bindService(serviceIntent, this, BIND_AUTO_CREATE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }
                    } else {
                        // 재시작
                        mLocationRecordService.runThread();

                        // bottomUpper result Animation (내려가기)
                        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
                        tblRecord.animate().translationY(100 * dpUnit).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                        clBottomUpperContainer.animate().translationY(100 * dpUnit).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                        tvProgress.animate().alpha(0.f).setInterpolator(interpolator).setDuration(500).start();
                        tvProgressUnit.animate().alpha(0.f).setInterpolator(interpolator).setDuration(500).start();
                        tvProgressDesc.animate().alpha(0.f).setInterpolator(interpolator).setDuration(500).start();
                        tvSubmitRecord.animate().alpha(0.f).setInterpolator(interpolator).setDuration(500).start();
                        ivStar1.animate().alpha(0.f).setInterpolator(interpolator).setDuration(200).start();
                        ivStar2.animate().alpha(0.f).setInterpolator(interpolator).setDuration(200).start();
                        ivStar3.animate().alpha(0.f).setInterpolator(interpolator).setDuration(200).start();
                    }

                } else {
                    // 운동 정지
                    SERVICE_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);

                    // 서비스 종료
                    mLocationRecordService.stopThread();

                    // bottomUpper result View 구성
                    tvProgress.setText(String.format("%.1f", mGoalPercent));

                    // bottomUpper result Animation (나타나기)
                    AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
                    tblRecord.animate().translationY(0).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                    clBottomUpperContainer.animate().translationY(0).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                    tvProgress.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvProgressUnit.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvProgressDesc.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvSubmitRecord.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    if (mGoalPercent >= 30) {
                        ivStar1.animate().alpha(1.f).setStartDelay(500).setDuration(200).start();
                    }
                    if (mGoalPercent >= 60){
                        ivStar2.animate().alpha(1.f).setStartDelay(600).setDuration(200).start();
                    }
                    if (mGoalPercent >= 90) {
                        ivStar3.animate().alpha(1.f).setStartDelay(700).setDuration(200).start();
                    }
                }
                break;
            case R.id.tv_submit_record:
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
                mvImplRecord.getLocationComponent().setCameraMode(CameraMode.TRACKING_GPS);
                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        mGoalPercent = (float) (mLocationDataSet.totalDistance / mGoal) * 100;
                        break;
                    case GOALTYPE_TIME:
                        mGoalPercent = (float) (mLocationDataSet.totalTime / mGoal) * 100;
                        break;
                }
                if (mGoalPercent >= 100)
                    mGoalPercent = 100;

                /* 새로운 polyline 점 추가 */
                int lastIndex = mLocationDataSet.locations.size() - 1;
                if (lastIndex >= 0) {
                    /* Set Line */
                    LatLng lastLatLng = mLocationDataSet.locations.get(lastIndex);
                    mPoints.add(Point.fromLngLat(lastLatLng.getLongitude(),lastLatLng.getLatitude(),lastLatLng.getAltitude()));
                    FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(mPoints)));

                    /* Set Gradient Stop Points*/
                    ArrayList<Expression.Stop> stops = new ArrayList<>();
                    for (Integer key : mLocationDataSet.changePowerIdxs.keySet()) {
                        Expression expression = mLocationDataSet.changePowerIdxs.get(key);
                        float percentage;
                        if (mLocationDataSet.locations.size() != 0) {
                            percentage = key / (float) mLocationDataSet.locations.size();
                        } else {
                            percentage = 0.f;
                        }
                        if (expression != null) {
                            stops.add(Expression.stop(percentage, expression));
                        }
                    }

                    /* Set Style */
                    GeoJsonSource path = mvStyle.getSourceAs("path");
                    if (path != null) {
                        path.setGeoJson(featureCollection);
                    }
                    LineLayer lines = mvStyle.getLayerAs("exercise");
                    if (lines != null) {
                        Expression.Stop[] stopExpressions = stops.toArray(new Expression.Stop[stops.size()]);
                        lines.setProperties(lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(5 * dpUnit),
                                lineGradient(Expression.interpolate(Expression.linear(),
                                        Expression.lineProgress(),
                                        stopExpressions)));
                    }

                    /* Debug TextView *//*
                    if (BuildConfig.DEBUG) {
                        ((TextView) findViewById(R.id.tv_debug))
                                .setText("Provider: " + cur.getProvider() + "\n"
                                        + "PrevLocation: " + prev.getLatitude() + ", " + prev.getLongitude() + "\n"
                                        + "CurLocation: " + cur.getLatitude() + ", " + cur.getLongitude() + "\n"
                                        + "Color: " + curPower.get(0) + ", " + curPower.get(1) + ", 0" + "\n"
                                        + "Velocity: " + cur.getSpeed() + " m/s\n"
                                        + "Accuracy: " + cur.getAccuracy() + "\n"
                                        + "DistanceTo: " + cur.distanceTo(prev) + " m\n"
                                        + "Distance(Vel): " + cur.getSpeed() * 1 + "m");
                    } */
                }


                /* Set TextView */
                String distance = "";
                String distanceUnit = "";
                if (mLocationDataSet.totalDistance < 1000) {
                    distance = String.format("%.1f", mLocationDataSet.totalDistance);
                    distanceUnit = "m";
                } else {
                    distance = String.format("%.1f", mLocationDataSet.totalDistance / 1000);
                    distanceUnit = "km";
                }
                Date date = new Date(mLocationDataSet.totalTime); // Time
                int pace = 0;
                if (mLocationDataSet.velocityAvg != 0)
                    pace = Math.round(1000 / mLocationDataSet.velocityAvg); // m/s -> sec/km

                tvDistance.setText(distance);
                tvDistanceUnit.setText(distanceUnit);
                tvTime.setText(CURRENT_TIME_FORMAT.format(date));
                tvPace.setText(String.format("%02d'%02d''", pace / 60, pace % 60));
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

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mvImplRecord.getStyle(this);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvRecord.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvRecord.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvRecord.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvRecord.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mvRecord.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvRecord.onLowMemory();
    }


}
