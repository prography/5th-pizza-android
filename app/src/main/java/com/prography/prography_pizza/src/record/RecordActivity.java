package com.prography.prography_pizza.src.record;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.prography.prography_pizza.BuildConfig;
import com.prography.prography_pizza.R;
import com.prography.prography_pizza.services.LocationRecordService;
import com.prography.prography_pizza.services.models.LocationDataSet;
import com.prography.prography_pizza.src.BaseActivity;
import com.prography.prography_pizza.src.common.utils.CustomPosNegDialog;
import com.prography.prography_pizza.src.common.utils.CustomSimpleMessageDialog;
import com.prography.prography_pizza.src.common.utils.CustomSubmitDialog;
import com.prography.prography_pizza.src.main.models.MainResponse;
import com.prography.prography_pizza.src.mypage.MyPageActivity;
import com.prography.prography_pizza.src.record.interfaces.RecordActivityView;
import com.prography.prography_pizza.src.tutorial.fragments.MypageHistoryFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.prography.prography_pizza.src.ApplicationClass.CURRENT_TIME_FORMAT;
import static com.prography.prography_pizza.src.ApplicationClass.USER_NAME;
import static com.prography.prography_pizza.src.ApplicationClass.sSharedPreferences;

public class RecordActivity extends BaseActivity implements RecordActivityView {

    public static final int GOALTYPE_DISTANCE = 1;
    public static final int GOALTYPE_TIME = 2;
    public static final float DEFAULT_LINE_WIDTH = 3;
    public static float dpUnit;

    private Toolbar tbRecord;
    private ActionBar abRecord;
    private TextView tvGoal;
    private ImageView ivStartRecord;
    public TextView tvSubmitRecord;
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
    public ProgressBar pbLoading;

    private ArrayList<Point> mPoints = new ArrayList<>();

    private Intent serviceIntent;
    private LocationRecordService mLocationRecordService;
    private MapboxMap mvImplRecord;
    private Style mvStyle;
    private PermissionsManager permissionsManager;
    private Bitmap mImg;

    public Bitmap getmImg() {
        return mImg;
    }

    private MainResponse.Data mChallenge;
    private double mGoal = 0;
    private int mGoalType = GOALTYPE_DISTANCE;
    private float mGoalPercent = 0.f;
    private float mGoalPercentLast = 0.f;
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
        pbLoading = findViewById(R.id.pb_loading_record);

        /* Get Intent */
        Intent intent = getIntent();
        mChallenge = intent.getParcelableExtra("challenge");
        mGoal = mChallenge.getTime();
        mGoalPercentLast = mChallenge.getAchievement();
        switch (mChallenge.getObjectUnit()) {
            case "distance":
                mGoalType = GOALTYPE_DISTANCE;
                switch (mChallenge.getExerciseType()) {
                    case "running":
                        tvGoal.setText((int) mGoal / 1000 + "km 달리기");
                        break;
                    case "cycling":
                        tvGoal.setText((int) mGoal / 1000 + "km 자전거 타기");
                        break;
                }
                break;
            case "time":
                mGoalType = GOALTYPE_TIME;
                switch (mChallenge.getExerciseType()) {
                    case "running":
                        tvGoal.setText((int) mGoal / 60 + "분 달리기");
                        break;
                    case "cycling":
                        tvGoal.setText((int) mGoal / 60 + "분 자전거 타기");
                        break;
                }
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

        /* Init View */
        clBottomUpperContainer.setTranslationY(100 * dpUnit);
        tblRecord.setTranslationY(100 * dpUnit);
        /* PieChart */
        List<PieEntry> pieEntryList = new ArrayList<>(Arrays.asList(
                new PieEntry(mGoalPercentLast / 100, ""),
                new PieEntry(1.f - mGoalPercentLast / 100, "")));
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
                    lineWidth(DEFAULT_LINE_WIDTH * dpUnit)));


            /* 액티비티 재시작일경우 기존 location 데이터로부터 Gradient Line 다시 그리기 */
            Intent intent = getIntent();
            if (intent.getParcelableExtra("locationDataSetInit") != null) {
                mLocationDataSet = intent.getParcelableExtra("locationDataSetInit");

                /* Set Line */
                mPoints.clear();
                for (LatLng latLng : mLocationDataSet.locations) {
                    mPoints.add(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude(), latLng.getAltitude()));
                }
                FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(mPoints)));

                /* Set Gradient Stop Points*/
                ArrayList<Expression.Stop> stops = new ArrayList<>();
                String colorLevel = "";
                for (int i = 0; i < mLocationDataSet.speeds.size(); i++) {
                    float vel = mLocationDataSet.speeds.get(i);
                    String curColorLevel = getColor(vel);
                    if (colorLevel.equals(getColor(vel))) {
                        // 같으면 패스
                        continue;
                    }
                    // 다를 때
                    float percentage;
                    if (mLocationDataSet.locations.size() != 0) {
                        percentage = i / (float) mLocationDataSet.locations.size();
                    } else {
                        percentage = 0.f;
                    }
                    stops.add(Expression.stop(percentage, Expression.color(Color.parseColor(curColorLevel))));
                    colorLevel = curColorLevel;
                }

                /* Set Style */
                GeoJsonSource path = style.getSourceAs("path");
                if (path != null) {
                    path.setGeoJson(featureCollection);
                }
                LineLayer lines = style.getLayerAs("exercise");
                if (lines != null) {
                    Expression.Stop[] stops1 = stops.toArray(new Expression.Stop[stops.size()]);
                    lines.setProperties(lineCap(Property.LINE_CAP_ROUND),
                            lineJoin(Property.LINE_JOIN_ROUND),
                            lineWidth(DEFAULT_LINE_WIDTH * dpUnit),
                            lineGradient(Expression.interpolate(Expression.linear(), Expression.lineProgress(),
                                    stops1)));
                }
            }

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private BroadcastReceiver mLocationDataSetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLocationDataSet = intent.getParcelableExtra("locationDataSet");

            /* Set View */
            if (mLocationDataSet != null) {
                mvImplRecord.getLocationComponent().setCameraMode(CameraMode.TRACKING_GPS);
                mvImplRecord.setCameraPosition(CameraUpdateFactory.zoomTo(16).getCameraPosition(mvImplRecord));
                switch (mGoalType) {
                    case GOALTYPE_DISTANCE:
                        mGoalPercent = mGoalPercentLast + (float) (mLocationDataSet.totalDistance / mGoal) * 100;
                        break;
                    case GOALTYPE_TIME:
                        mGoalPercent = mGoalPercentLast + (float) (mLocationDataSet.totalTime / mGoal) * 100;
                        break;
                }
                if (mGoalPercent >= 100)
                    mGoalPercent = 100;

                /* 새로운 polyline 점 추가 */
                int lastIndex = mLocationDataSet.locations.size() - 1;
                if (lastIndex >= 0) {
                    /* Set Line */
                    LatLng lastLatLng = mLocationDataSet.locations.get(lastIndex);
                    mPoints.add(Point.fromLngLat(lastLatLng.getLongitude(), lastLatLng.getLatitude(), lastLatLng.getAltitude()));
                    FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(LineString.fromLngLats(mPoints)));

                    /* Set Gradient Stop Points*/
                    ArrayList<Expression.Stop> stops = new ArrayList<>();
                    String colorLevel = "";
                    for (int i = 0; i < mLocationDataSet.speeds.size(); i++) {
                        float vel = mLocationDataSet.speeds.get(i);
                        String curColorLevel = getColor(vel);
                        if (colorLevel.equals(getColor(vel))) {
                            // 같으면 패스
                            continue;
                        }
                        // 다를 때
                        float percentage;
                        if (mLocationDataSet.locations.size() != 0) {
                            percentage = i / (float) mLocationDataSet.locations.size();
                        } else {
                            percentage = 0.f;
                        }
                        stops.add(Expression.stop(percentage, Expression.color(Color.parseColor(curColorLevel))));
                        colorLevel = curColorLevel;
                    }

                    /* Set Style */
                    GeoJsonSource path = mvStyle.getSourceAs("path");
                    if (path != null) {
                        path.setGeoJson(featureCollection);
                    }
                    LineLayer lines = mvStyle.getLayerAs("exercise");
                    if (lines != null) {
                        Expression.Stop[] stops1 = stops.toArray(new Expression.Stop[stops.size()]);
                        lines.setProperties(lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(DEFAULT_LINE_WIDTH * dpUnit),
                                lineGradient(Expression.interpolate(Expression.linear(),
                                        Expression.lineProgress(),
                                        stops1)));
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
                Date date = new Date(mLocationDataSet.totalTime * 1000); // Time
                int pace = 0;
                if (mLocationDataSet.speedAvg != 0)
                    pace = Math.round(1000 / mLocationDataSet.speedAvg); // m/s -> sec/km

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

    /**
     * @param vel velocity of the location
     * @return the value of String
     */
    public String getColor(float vel) {
        String red = "00", green = "00", blue = "00";

        if (vel < LocationRecordService.SPEED_RANGE0) {
            // 5km/h 미만
            red = "FF";
        } else if (vel < LocationRecordService.SPEED_RANGE1) {
            // 10km/h 미만
            red = "FF";
            green = "FF";
        } else if (vel < LocationRecordService.SPEED_RANGE2) {
            // 15km/h 미만
            red = "FF";
            green = "FF";
        } else if (vel >= LocationRecordService.SPEED_RANGE2) {
            blue = "FF";
        }
        return "#" + red + green + blue;
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
                tryFinish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        tryFinish();
    }

    private void tryFinish() {
        CustomPosNegDialog cpndFinish = new CustomPosNegDialog.Builder(this)
                .setMessage("운동을 저장하지 않고 종료하시겠습니까?")
                .setType(CustomPosNegDialog.FINISH_ACTIVITY)
                .build();
        cpndFinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cpndFinish.show();
    }

    public void tryPostImgToFirebase(Bitmap bitmap) {
        showProgressDialog();
        RecordService recordService = new RecordService(this);
        recordService.postImgToFirebase(sSharedPreferences.getString(USER_NAME, "name"), bitmap);
    }

    public void tryPostRecord(int challengeId, double totalTime, double totalDistance, String latLngs) {
        RecordService recordService = new RecordService(this);
        recordService.postRecord(challengeId, totalTime, totalDistance, latLngs);
    }

    @Override
    public void validateSuccess(String message) {
        hideProgressDialog();
        pbLoading.setVisibility(View.GONE);
        pbLoading.setIndeterminate(false);
        pbLoading.setProgress(100, true);
        CustomSimpleMessageDialog csmdSuccess = new CustomSimpleMessageDialog.Builder(this)
                .setMessage("업로드에 성공하였습니다.")
                .setType(CustomSimpleMessageDialog.FINISH_ACTIVITY_THEN_START)
                .setNextActivity(MyPageActivity.class)
                .setButtonText("확인")
                .build();
        csmdSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        csmdSuccess.setCancelable(false);
        csmdSuccess.show();
    }

    @Override
    public void validateFirebaseSuccess(String imgUrl) {
        tryPostRecord(mChallenge.getChallengeId(), mLocationDataSet.totalTime, (double) mLocationDataSet.totalDistance, imgUrl);
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        pbLoading.setVisibility(View.GONE);
        tvSubmitRecord.setText("SUBMIT");
        tvSubmitRecord.setEnabled(true);
        showSimpleMessageDialog("업로드에 실패하였습니다.", "확인", CustomSimpleMessageDialog.FINISH_NONE, null);
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
                        tvSubmitRecord.setEnabled(false);
                    }

                } else {
                    // 운동 정지
                    SERVICE_RUNNING = false;
                    ivStartRecord.setImageResource(R.drawable.ic_start);

                    // 서비스 종료
                    mLocationRecordService.stopThread();

                    // bottomUpper result View 구성
                    tvProgress.setText(String.format("%.1f", mGoalPercent));

                    // MapView 축소.
                    if (mLocationDataSet.locations.size() > 1) {
                        LatLngBounds latLngBounds = new LatLngBounds.Builder().includes(mLocationDataSet.locations).build();
                        mvImplRecord.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
                    }

                    // bottomUpper result Animation (나타나기)
                    AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
                    tblRecord.animate().translationY(0).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                    clBottomUpperContainer.animate().translationY(0).setStartDelay(20).setInterpolator(interpolator).setDuration(500).start();
                    tvProgress.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvProgressUnit.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvProgressDesc.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvSubmitRecord.animate().alpha(1.f).setInterpolator(interpolator).setDuration(500).start();
                    tvSubmitRecord.setEnabled(true);
                    if (mGoalPercent >= 30) {
                        ivStar1.animate().alpha(1.f).setStartDelay(500).setDuration(200).start();
                    }
                    if (mGoalPercent >= 60) {
                        ivStar2.animate().alpha(1.f).setStartDelay(600).setDuration(200).start();
                    }
                    if (mGoalPercent >= 90) {
                        ivStar3.animate().alpha(1.f).setStartDelay(700).setDuration(200).start();
                    }
                }
                break;
            case R.id.tv_submit_record:
                tvSubmitRecord.setPivotX(tvSubmitRecord.getWidth());
                tvSubmitRecord.animate()
                        .setDuration(200)
                        .setInterpolator(new DecelerateInterpolator(1.f))
                        .scaleX(0.3f)
                        .withStartAction(new Runnable() {
                            @Override
                            public void run() {
                                tvSubmitRecord.setTextColor(Color.parseColor("#ea7a58"));
                            }
                        })
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                tvSubmitRecord.setText("");
                                tvSubmitRecord.setScaleX(1.f);
                                pbLoading.setVisibility(View.VISIBLE);
                            }
                        })
                        .start();

                tvSubmitRecord.setEnabled(false);
                LatLngBounds latLngBounds = null;
                if (mLocationDataSet.locations.size() > 1) {
                    latLngBounds = new LatLngBounds.Builder().includes(mLocationDataSet.locations).build();
                } else if (mLocationDataSet.locations.size() == 1) {
                    latLngBounds = new LatLngBounds.Builder().include(mLocationDataSet.locations.get(0)).build();
                } else {
                    break;
                }
                mvImplRecord.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
                MapSnapshotter.Options options = new MapSnapshotter.Options(500, 500)
                        .withRegion(mvImplRecord.getProjection().getVisibleRegion().latLngBounds)
                        .withStyle(mvStyle.getUri())
                        .withLogo(false);
                MapSnapshotter mapSnapshotter = new MapSnapshotter(this, options);
                mapSnapshotter.start(this);
                break;
        }
    }

    @Override
    public void onSnapshotReady(MapSnapshot snapshot) {
        mImg = extractMapImageView(snapshot);

        float minGoal = 0;
        if (!BuildConfig.DEBUG)
            minGoal = 3.f;

        if (mGoalPercent == 100) {
            // 1. 목표를 달성했을 때.
            CustomSubmitDialog customSubmitDialog = new CustomSubmitDialog(this, 3, mGoalPercent, "목표를 달성했어요!\n이제 저장하고 쉴까요?");
            customSubmitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customSubmitDialog.show();
        } else if (mGoalPercent >= minGoal) {
            // 2. 충분한 거리를 달렸지만 목표를 달성하지 못했을 때.
            int starCount;
            if (mGoalPercent >= 90) {
                starCount = 3;
            } else if (mGoalPercent >= 60) {
                starCount = 2;
            } else if (mGoalPercent >= 30) {
                starCount = 1;
            } else {
                starCount = 0;
            }
            CustomSubmitDialog customSubmitDialog = new CustomSubmitDialog(this, starCount, mGoalPercent, "목표를 아직 달성하지 못했어요.\n여기까지만 저장하고 잠시 쉴까요?");
            customSubmitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customSubmitDialog.show();
        } else {
            // 3. 달린 거리나 시간이 너무 부족할 때, (3.0% 미만)
            showSimpleMessageDialog("너무 조금만 달렸는데요?\n조금만 더 해볼까요?", getString(R.string.tv_positive), CustomSimpleMessageDialog.FINISH_NONE, null);
        }
    }

    public Bitmap extractMapImageView(MapSnapshot input) {
        showProgressDialog();
        Bitmap output = input.getBitmap();
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, output.getWidth(), output.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH * dpUnit);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

        /* Get Color Gradient Array */
        // TODO : paint를 LineGradient shader를 활용해서 원래 이미지랑 똑같이 복원하기.

        canvas.drawBitmap(output, null, rect, paint);
        for (int i = 0; i < mLocationDataSet.locations.size(); i++ ) {
            if (i < mLocationDataSet.locations.size() - 2) {
                LatLng latLng = mLocationDataSet.locations.get(i);
                LatLng latLngNext = mLocationDataSet.locations.get(i + 1);
                PointF point = input.pixelForLatLng(latLng);
                PointF pointNext = input.pixelForLatLng(latLngNext);
                paint.setColor(Color.parseColor(getColor(mLocationDataSet.speeds.get(i))));
                canvas.drawLine(point.x, point.y, pointNext.x, pointNext.y, paint);
            }

        }
        hideProgressDialog();
        return output;
    }

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
