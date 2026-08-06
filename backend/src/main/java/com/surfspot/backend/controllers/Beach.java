package com.surfspot.backend.controllers;

public class Beach {
    private String beachName;
    private int waterTemp;
    public Beach(String beachName, int waterTemp) {
        this.beachName = beachName;
        this.waterTemp = waterTemp;
    }
    public String getBeachName() {
        return beachName;
    }
    public int getWaterTemp() {
        return waterTemp;
    }
}
