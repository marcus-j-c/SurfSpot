package com.surfspot.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity /* treat this java class as a postgres database table */
@Table(name = "surf_cache") /* name the database */
public class SurfCache {
    // id is the primary key column for this table
    @Id
    // generated value means postgres uses auto increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // spot id cant be null, and has to be unique
    @Column(nullable = false, unique = true)
    private String spotId;
    private Double latitude;
    private Double longitude;
    // TEXT overrides 255 char limit, so can store json strings from my apis
    @Column(columnDefinition = "TEXT")
    private String cachedData;
    private Instant lastUpdated;

    public SurfCache() {
    }

    public SurfCache(String spotId, Double latitude, Double longitude, String cachedData, Instant lastUpdated) {
        this.spotId = spotId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cachedData = cachedData;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public String getSpotId() {
        return spotId;
    }

    public String getCachedData() {
        return cachedData;
    }

    public void setCachedData(String cachedData) {
        this.cachedData = cachedData;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
