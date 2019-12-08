package com.prography.prography_pizza.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.prography.prography_pizza.src.main.models.ChallengeResponse;

import java.util.List;

@Dao
public interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChallengeResponse.Data datum);

    @Query("SELECT * FROM Data ORDER BY challengeId DESC")
    List<ChallengeResponse.Data> getAll();

    @Query("SELECT * FROM Data WHERE challengeId = :id")
    List<ChallengeResponse.Data> findDatum(int id);

    @Delete
    void delete(ChallengeResponse.Data datum);
}
