package com.prography.prography_pizza.src.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainDeleteResponse {
    @SerializedName("data")
    private int data;

    public int getData() {
        return data;
    }
}
