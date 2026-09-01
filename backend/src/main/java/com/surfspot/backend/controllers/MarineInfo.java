package com.surfspot.backend.controllers;

import java.util.List;

public record MarineInfo (HourlyMarine hourly) {
    public record HourlyMarine(List<String> time, List<Double> wave_height, List<Double> wave_period, List<Integer> wave_direction, List<Double> sea_surface_temperature) {}
}
