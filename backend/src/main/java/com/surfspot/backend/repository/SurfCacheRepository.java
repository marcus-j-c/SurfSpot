package com.surfspot.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surfspot.backend.model.SurfCache;

// each item in the table is a SurfCache, search by spotId
public interface SurfCacheRepository extends JpaRepository<SurfCache, Long> {
    Optional<SurfCache> findBySpotId(String spotId);
}
