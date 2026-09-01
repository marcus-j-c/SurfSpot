package com.surfspot.backend.controllers;

public record BeachInfo(Long id, String name, double rating, double waveHeight, double wavePeriod, double windSpeed, String windDirection, double tide, double waterTemp, String weather, String reasoning, String goodStuff, String badStuff, String forecastDateTime, String sunrise, String sunset) {}