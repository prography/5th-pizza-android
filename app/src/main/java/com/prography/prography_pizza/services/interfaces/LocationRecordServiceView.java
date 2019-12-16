package com.prography.prography_pizza.services.interfaces;

import android.location.LocationListener;

public interface LocationRecordServiceView extends LocationListener {
    void runThread();
    void stopThread();
}
