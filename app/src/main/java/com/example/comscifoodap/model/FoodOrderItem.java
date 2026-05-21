package com.example.comscifoodap.model;

import com.google.gson.Gson;

import java.util.HashMap;

public class FoodOrderItem {
    private int id;
    private String gsonData;
    private boolean isReady;

    // Constructors, getters, and setters

    public FoodOrderItem(int id, HashMap<String, Integer> dataList, boolean isReady) {
        this.id = id;
        this.gsonData = new Gson().toJson(dataList);
        this.isReady = isReady;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGsonData() {
        return gsonData;
    }

    public void setGsonData(HashMap<String, Integer> dataList) {
        this.gsonData = new Gson().toJson(dataList);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        this.isReady = ready;
    }
}
