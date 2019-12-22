package com.prography.prography_pizza.db;

import android.content.Context;

import com.prography.prography_pizza.services.models.LocationDataSet;

import java.util.ArrayList;

public class SavedLocationDataModel {

    private SavedLocationDataDao savedLocationDataDao;

    public SavedLocationDataModel(Context context) {
        savedLocationDataDao = SavedLocationDataDatabase.getDatabase(context).savedLocationDataDao();
    }

    public void save(final LocationDataSet locationDataSet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                savedLocationDataDao.save(locationDataSet);
            }
        }).start();
    }

    public void delete() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                savedLocationDataDao.delete(get());
            }
        }).start();
    }

    public LocationDataSet get() {
        final ArrayList<LocationDataSet> locationDataSets = new ArrayList<>();
        Thread getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDataSets.addAll(savedLocationDataDao.get());
            }
        });
        getThread.start();

        try {
            getThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (locationDataSets.size() != 0)
            return locationDataSets.get(0);
        else
            return null;
    }
}
