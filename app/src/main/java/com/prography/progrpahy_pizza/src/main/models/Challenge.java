package com.prography.progrpahy_pizza.src.main.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "challenges")
public class Challenge {
    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    private String routineType;
    private int time;
    private String objectUnit;
    private String exerciseType;

    public Challenge(){

    }

    public Challenge(String routineType, int time, String objectUnit, String exerciseType) {
        this.routineType = routineType;
        this.time = time;
        this.objectUnit = objectUnit;
        this.exerciseType = exerciseType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getObjectUnit() {
        return objectUnit;
    }

    public void setObjectUnit(String objectUnit) {
        this.objectUnit = objectUnit;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }
}
