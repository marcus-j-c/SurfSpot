package com.surfspot.backend.controllers;

public record BeachInfo (String beachName, String forecastDateTime, double waveHeight, int wavePeriod, double windSpeed,String windDirection, double currentTideHeight, double airTemp, double waterTemp, String weatherCondition, String sunrise, String sunset) {}
