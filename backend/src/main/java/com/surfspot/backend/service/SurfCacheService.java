package com.surfspot.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.surfspot.backend.model.SurfCache;
import com.surfspot.backend.repository.SurfCacheRepository;

@Service
public class SurfCacheService {

    private final SurfCacheRepository cacheRepository;

    public SurfCacheService(SurfCacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public Optional<SurfCache> getCache(String spotId) {
        return cacheRepository.findBySpotId(spotId);
    }

    // is the cache less than an hour old
    public boolean isFresh(SurfCache cache) {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        return cache.getLastUpdated().isAfter(oneHourAgo);
    }

    public void saveOrUpdateCache(String spotId, String rawData, Double latitude, Double longitude) {
        Optional<SurfCache> existingOpt = cacheRepository.findBySpotId(spotId);

        SurfCache cache;
        // if the SurfCache exitst update it
        if (existingOpt.isPresent()) {
            cache = existingOpt.get();
            cache.setCachedData(rawData);
            cache.setLastUpdated(Instant.now());
            cache.setLatitude(latitude);
            cache.setLongitude(longitude);
        } else {
            // else create the SurfCache for this location
            cache = new SurfCache(spotId, latitude, longitude, rawData, Instant.now());
        }

        cacheRepository.save(cache);
    }
}
