package com.prography.prography_pizza.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.prography.prography_pizza.services.models.LocationDataSet;

import java.util.List;

@Dao
public interface SavedLocationDataDao {
    @Insert
    void save(LocationDataSet locationDataSet);

    @Query("SELECT * FROM LocationDataSet")
    List<LocationDataSet> get();

    @Delete
    void delete(LocationDataSet locationDataSet);
}
