package com.prography.prography_pizza.src.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainDeleteResponse {
    @SerializedName("data")
    private MainResponse.Data data;

    public MainResponse.Data getData() {
        return data;
    }
}
