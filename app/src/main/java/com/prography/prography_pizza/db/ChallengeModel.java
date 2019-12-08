package com.prography.prography_pizza.db;

import android.content.Context;

import com.prography.prography_pizza.src.main.models.ChallengeResponse;

import java.util.ArrayList;

public class ChallengeModel {
    private ChallengeDao challengeDao;

    public ChallengeModel(Context context) {
        challengeDao = ChallengeDatabase.getDatabase(context).challengeDao();
    }

    public void insertData(final ArrayList<ChallengeResponse.Data> data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ChallengeResponse.Data datum : data) {
                    challengeDao.insert(datum);
                }
            }
        }).start();
    }

    public void insertDatum(final ChallengeResponse.Data datum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                challengeDao.insert(datum);
            }
        }).start();
    }

    public ArrayList<ChallengeResponse.Data> getAll() {
        final ArrayList<ChallengeResponse.Data> data = new ArrayList<>();

        Thread getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                data.addAll(challengeDao.getAll());
            }
        });
        getThread.start();

        try {
            getThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return data;
    }

    public ChallengeResponse.Data findDatum(final int cid) {
        final ArrayList<ChallengeResponse.Data> data = new ArrayList<>();
        Thread getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                data.addAll(challengeDao.findDatum(cid));
            }
        });
        getThread.start();

        try {
            getThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return data.get(0);
    }

    public void delete(int cid) {
        final ChallengeResponse.Data datum = findDatum(cid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                challengeDao.delete(datum);
            }
        }).start();
    }
}
