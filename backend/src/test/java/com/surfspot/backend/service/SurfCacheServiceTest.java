package com.surfspot.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.surfspot.backend.model.SurfCache;
import com.surfspot.backend.repository.SurfCacheRepository;

// start mockito
@ExtendWith(MockitoExtension.class)
class SurfCacheServiceTest {

    // create fake version of the repository
    @Mock
    private SurfCacheRepository cacheRepository;

    // use the real surfCacheService and wire the mock above into it
    @InjectMocks
    private SurfCacheService surfCacheService;

    @Test
    void isFresh_ShouldReturnTrue_WhenUnderOneHourOld() {
        // fake cache that was supossedly made 30 mins ago
        SurfCache cache = new SurfCache("banzai-pipeline-beach", 0.0, 0.0, "{}",
                Instant.now().minus(30, ChronoUnit.MINUTES));
        // run is fresh, and assert that it returns true, if so pass the test
        assertTrue(surfCacheService.isFresh(cache));
    }

    @Test
    void isFresh_ShouldReturnFalse_WhenOverOneHourOld() {
        // same but 61 mins old
        SurfCache cache = new SurfCache("banzai-pipeline-beach", 0.0, 0.0, "{}",
                Instant.now().minus(61, ChronoUnit.MINUTES));
        // make sure it returns false, if so pass
        assertFalse(surfCacheService.isFresh(cache));
    }

    @Test
    void getCache_ShouldReturnCache_WhenKeyExists() {
        SurfCache cache = new SurfCache("banzai-pipeline-beach", 0.0, 0.0, "{}", Instant.now());
        // when you try to find the spot, use my fake cache instead of the real db
        when(cacheRepository.findBySpotId("banzai-pipeline-beach")).thenReturn(Optional.of(cache));
        Optional<SurfCache> result = surfCacheService.getCache("banzai-pipeline-beach");
        assertTrue(result.isPresent());
        assertEquals("banzai-pipeline-beach", result.get().getSpotId());
    }
}
