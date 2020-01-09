package com.prography.prography_pizza.src.record.interfaces;

import android.content.ServiceConnection;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;

public interface RecordActivityView extends View.OnClickListener,
        com.mapbox.mapboxsdk.maps.OnMapReadyCallback,
        com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded,
        PermissionsListener,
        ServiceConnection,
        MapSnapshotter.SnapshotReadyCallback {

    void validateSuccess(String message);

    void validateFirebaseSuccess(String imgUrl);

    void validateFailure(String message);
}
