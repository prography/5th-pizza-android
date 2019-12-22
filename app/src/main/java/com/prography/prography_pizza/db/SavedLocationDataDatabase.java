package com.prography.prography_pizza.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.prography.prography_pizza.services.models.LocationDataSet;

@Database(entities = {LocationDataSet.class}, version = 1, exportSchema = false)
public abstract class SavedLocationDataDatabase extends RoomDatabase {
    private static volatile SavedLocationDataDatabase database;

    static SavedLocationDataDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (SavedLocationDataDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), SavedLocationDataDatabase.class, "savedLocationData").build();
                }
            }
        }
        return database;
    }

    abstract public SavedLocationDataDao savedLocationDataDao();
}
