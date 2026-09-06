package com.surfspot.backend.controllers;

import java.util.List;

public record ForecastInfo(List<Weather> weather, Main main, Wind wind, Sys sys) {
    public record Weather(int id, String main, String description) {}
    public record Main(double temp) {}
    public record Wind(double speed, int deg) {}
    public record Sys(long sunrise, long sunset) {}
}