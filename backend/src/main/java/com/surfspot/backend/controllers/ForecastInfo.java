package com.surfspot.backend.controllers;

import java.util.List;

public record ForecastInfo(HourlyForecast hourly, DailyForecast daily) {
    public record HourlyForecast(List<String> time, List<Double> temperature_2m, List<Double> wind_speed_10m, List<Integer> wind_direction_10m, List<Integer> weathercode) {}
    public record DailyForecast(List<String> time, List<String> sunrise, List<String> sunset) {}
}