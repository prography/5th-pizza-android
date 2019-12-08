package com.prography.prography_pizza.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.prography.prography_pizza.src.main.models.ChallengeResponse;

@Database(entities = {ChallengeResponse.Data.class}, version = 1, exportSchema = false)
public abstract class ChallengeDatabase extends RoomDatabase {
    private static volatile ChallengeDatabase database;

    static ChallengeDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (ChallengeDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), ChallengeDatabase.class, "challenges").build();
                }
            }
        }
        return database;
    }

    abstract public ChallengeDao challengeDao();
}
