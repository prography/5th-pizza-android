package com.prography.progrpahy_pizza.src.main.models;

import com.prography.progrpahy_pizza.src.common.models.DefaultResponse;

public class ChallengeResponse extends DefaultResponse {
    private String routineType;
    private double time;
    private String objectUnit;
    private String exerciseType;

    public ChallengeResponse(String routineType, double time, String objectUnit, String exerciseType) {
        this.routineType = routineType;
        this.time = time;
        this.objectUnit = objectUnit;
        this.exerciseType = exerciseType;
    }

    public String getRoutineType() {
        return routineType;
    }

    public double getTime() {
        return time;
    }

    public String getObjectUnit() {
        return objectUnit;
    }

    public String getExerciseType() {
        return exerciseType;
    }
}
