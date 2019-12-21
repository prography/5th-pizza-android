package com.prography.prography_pizza.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prography.prography_pizza.src.main.models.MainResponse;

import java.util.List;

@Dao
public interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MainResponse.Data datum);

    @Query("SELECT * FROM Data ORDER BY challengeId DESC")
    List<MainResponse.Data> getAll();

    @Query("SELECT * FROM Data WHERE achievement < 100 ORDER BY challengeId DESC")
    List<MainResponse.Data> getAllUnCompleted();

    @Query("SELECT * FROM Data WHERE achievement >= 100 ORDER BY challengeId DESC")
    List<MainResponse.Data> getAllCompleted();

    @Query("SELECT * FROM Data WHERE challengeId = :id")
    List<MainResponse.Data> findDatum(int id);

    @Delete
    void delete(MainResponse.Data datum);
}
