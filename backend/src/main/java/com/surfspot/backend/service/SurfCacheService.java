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

    public Optional<SurfCache> getFreshCache(String spotId) {
        Optional<SurfCache> cacheOpt = cacheRepository.findBySpotId(spotId);

        // could i find the SurfCache in my database
        if (cacheOpt.isPresent()) {
            SurfCache cache = cacheOpt.get();
            Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);

            // if so, was it last updated in the past hour
            if (cache.getLastUpdated().isAfter(oneHourAgo)) {
                return Optional.of(cache);
            }
        }

        // if neither of those are true, remturn empty
        return Optional.empty();
    }

    public void saveOrUpdateCache(String spotId, String rawData) {
        Optional<SurfCache> existingOpt = cacheRepository.findBySpotId(spotId);

        SurfCache cache;
        // if the cache already existed it was just outdated, update the data inside it
        if (existingOpt.isPresent()) {
            cache = existingOpt.get();
            cache.setCachedData(rawData);
            cache.setLastUpdated(Instant.now());
        } else {
            // otherwise make a new cache
            cache = new SurfCache(spotId, rawData, Instant.now());
        }

        // save the new cache, or over the existing cache with the data
        cacheRepository.save(cache);
    }
}
