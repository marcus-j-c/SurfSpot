package com.surfspot.backend.controllers;

import java.util.List;

public record GeocodingInfo(List<BeachCoords> results) {
    public record BeachCoords(String name, double latitude, double longitude) {}
}
