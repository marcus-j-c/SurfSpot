package com.surfspot.backend.controllers;

import java.text.Normalizer;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.surfspot.backend.model.SurfCache;
import com.surfspot.backend.service.SurfCacheService;

import tools.jackson.databind.ObjectMapper;

// this allows react to fetch the data from the backend
@CrossOrigin(origins = { "https://surf-spot-ruddy.vercel.app", "http://localhost:5173" })
@RestController
@RequestMapping("/beaches")
public class BeachController {
    private static final Logger log = LoggerFactory.getLogger(BeachController.class);

    private record GeocodingResult(GeocodingInfo info, String displayName, String matchedKey) {
    }

    private final RestClient restClient = RestClient.create(); // create a RestClient instance
    @Value("${LOCATIONIQ_KEY}")
    private String locationIqKey;
    @Value("${OWM_API_KEY}")
    private String oWMKey;
    private final SurfCacheService surfCacheService;
    private final ObjectMapper objectMapper;
    private final AtomicLong cacheHits = new AtomicLong(0);
    private final AtomicLong cacheMisses = new AtomicLong(0);

    @GetMapping
    public BeachInfo getBeachByName(@RequestParam String name) {
        List<String> nameVariations = generateNameVars(name);

        for (String key : nameVariations) {
            Optional<SurfCache> cacheOpt = surfCacheService.getCache(key);
            if (cacheOpt.isPresent()) {
                cacheHits.incrementAndGet();
                SurfCache cache = cacheOpt.get();
                if (surfCacheService.isFresh(cache) == true) {
                    log.info("Fresh Cache HIT for key: '{}'", key);
                    try {
                        return objectMapper.readValue(cache.getCachedData(), BeachInfo.class);
                    } catch (Exception e) {
                        log.error("Failed to parse cached JSON for {}: {}", key, e.getMessage());
                    }
                }
                log.info("Stale Cache HIT for key: '{}'. Fetching marine/weather with stored coords.", key);
                String cleanedName = Arrays.stream(name.split("-"))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining(" "));
                GeocodingInfo.BeachCoords staleHitInfo = new GeocodingInfo.BeachCoords("stale", cache.getLatitude(),
                        cache.getLongitude());
                GeocodingResult staleHit = new GeocodingResult(new GeocodingInfo(List.of(staleHitInfo)), cleanedName,
                        key);
                BeachInfo freshData = getBeachData(staleHit.info(), staleHit.displayName());
                try {
                    String jsonString = objectMapper.writeValueAsString(freshData);
                    surfCacheService.saveOrUpdateCache(key, jsonString, cache.getLatitude(), cache.getLongitude());
                } catch (Exception e) {
                    log.error("Failed to update stale cache for {}: {}", key, e.getMessage());
                }
                return freshData;
            }
        }
        cacheMisses.incrementAndGet();
        String cleanedName = Arrays.stream(name.split("-"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
        log.info("Cache MISS for spot: '{}' (variants tried: {}). Fetching API data.", cleanedName,
                nameVariations);
        // collect(Collectors.joining(" ")) tells to join with spaces.
        // substring(0,1) grabs first char, as that is 0 up to but not including 1,
        // then substring(1) grabs the rest of the string starting at index 1.
        // And map just applies this capitalisation to each word.
        // This is the same as i did in TS on my frontend.
        GeocodingResult res = coordsRequest(name, cleanedName);
        if (res.info() == null || res.info().results() == null || res.info().results().isEmpty() == true) {
            return getBeachData(null, cleanedName);
        }

        BeachInfo freshBeachData = getBeachData(res.info(), res.displayName());

        try {
            String jsonString = objectMapper.writeValueAsString(freshBeachData);
            String cacheKey = res.matchedKey() != null ? res.matchedKey() : name.toLowerCase();
            surfCacheService.saveOrUpdateCache(cacheKey, jsonString, res.info().results().get(0).latitude(),
                    res.info().results().get(0).longitude());
            log.info("Saved fresh cache entry for: {}", cacheKey);
        } catch (Exception e) {
            log.error("Failed to serialise cache payload: {}", e.getMessage());
        }

        return freshBeachData;
    }

    private GeocodingResult coordsRequest(String name, String displayName) {
        // Normalizer.normalize(name, Normalizer.Form.NFD) split the accent mark and the
        // letter its on into 2 characters replaceAll("\\p{M}", "") vaporises all split
        // off accent marks, replace hyphens with spaces, convert to lowercase, and trim
        // whitespaces.
        String cleanedName = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("-", " ")
                .toLowerCase().trim();
        log.info("Searching geocoding API for cleaned name: '{}'", cleanedName);
        // try costal search first
        String searchTarget = cleanedName.contains("beach") ? cleanedName : cleanedName + " beach";
        BackupGeocodingInfo[] locationIqResponse = null;
        for (int i = 0; i < 3; i++) { // try locationIq up to 3 times in case of a causing failure network error.
            try {
                locationIqResponse = restClient.get().uri("https://us1.locationiq.com/v1/search?key=" + locationIqKey
                        + "&q=" + searchTarget + "&format=json").retrieve().body(BackupGeocodingInfo[].class);
                break; // if it works get out of the loop early
            } catch (Exception e) {// if it fails, just try again, up to 3 times.
                log.warn("locationIq request failed on attempt {}: {}", i + 1, e.getMessage());
            }
        }
        log.info("locationIq geocoding response: {}", locationIqResponse);
        if (locationIqResponse != null && locationIqResponse.length > 0) { // check if the response isnt empty.
            // sort locationIqResponse by importance from highest to lowest
            Arrays.sort(locationIqResponse, Comparator.comparingDouble(BackupGeocodingInfo::importance).reversed());
            // temporary before dropdown box on frontend search bar, this would not work tho
            // cause locationIq name field is dsplay_name, wheras nominatims is name, wasnt
            // doing this in v1 anyway so not a bug rn
            BackupGeocodingInfo firstResult = locationIqResponse[0];
            GeocodingInfo.BeachCoords coords = new GeocodingInfo.BeachCoords(firstResult.name(),
                    // convert the locationIqResponse to GeocodingInfo record, bc this is the
                    // fastest fix after swapping out open meteo
                    Double.parseDouble(firstResult.lat()), Double.parseDouble(firstResult.lon()));
            // return the geocoding info record with the coords in a list
            return new GeocodingResult(new GeocodingInfo(List.of(coords)), displayName, searchTarget.replace(" ", "-"));
        }
        log.info("No locationIq results, falling back to Nominatim for '{}'", cleanedName);
        try { // if the response is empty, try nominatim as my backup, with beach first to
              // target the coast, then the original name.
            BackupGeocodingInfo[] nominatimResponse = restClient.get()
                    .uri("https://nominatim.openstreetmap.org/search?q=" + searchTarget + "&format=json")
                    .header("User-Agent", "SurfSpot/V1 (https://surf-spot-ruddy.vercel.app)").retrieve()
                    .body(BackupGeocodingInfo[].class);
            String usedTarget = searchTarget;
            if ((nominatimResponse == null || nominatimResponse.length == 0) && !searchTarget.equals(cleanedName)) {
                usedTarget = cleanedName;
                nominatimResponse = restClient.get()
                        .uri("https://nominatim.openstreetmap.org/search?q=" + cleanedName + "&format=json")
                        .header("User-Agent", "SurfSpot/V1 (https://surf-spot-ruddy.vercel.app)").retrieve()
                        .body(BackupGeocodingInfo[].class);
            }
            if (nominatimResponse != null && nominatimResponse.length > 0) {
                Arrays.sort(nominatimResponse, Comparator.comparingDouble(BackupGeocodingInfo::importance).reversed());
                BackupGeocodingInfo firstResult = nominatimResponse[0];
                GeocodingInfo.BeachCoords coords = new GeocodingInfo.BeachCoords(firstResult.name(),
                        Double.parseDouble(firstResult.lat()), Double.parseDouble(firstResult.lon()));
                return new GeocodingResult(new GeocodingInfo(List.of(coords)), displayName,
                        usedTarget.replace(" ", "-"));
            }
        } catch (Exception e) {
            log.warn("Nominatim lookup failed: {}", e.getMessage());
        }
        final String[] suffixes = { "beach", "spot", "point", "break", "reef", "surf" };
        String[] doubleCleanedName = cleanedName.split(" ");
        for (String suffix : suffixes) {
            if (doubleCleanedName[doubleCleanedName.length - 1].equalsIgnoreCase(suffix)) {
                String cleanedNameWithoutSuffix = String.join(" ",
                        // remove the suffix from the name
                        Arrays.copyOf(doubleCleanedName, doubleCleanedName.length - 1));
                log.info("Searching geocoding API for cleaned name without suffix: '{}'", cleanedNameWithoutSuffix);
                String newDisplayName = Arrays.stream(cleanedNameWithoutSuffix.split(" "))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining(" "));
                // recursively call coords request with the cleaned name without the suffix
                return coordsRequest(cleanedNameWithoutSuffix, newDisplayName);
            }
        }
        log.info("No results found for '{}', returning null", cleanedName);
        return new GeocodingResult(null, displayName, null); // if all else fails, return null
    }

    private double safeDouble(List<Double> list, int currentUtcHour) {
        // if the list doesnt exist, is empty or the first element is null return -1,
        // else return the first element
        return (list != null && list.size() > currentUtcHour && list.get(currentUtcHour) != null)
                ? list.get(currentUtcHour)
                : -1.0;
    }

    private double round1dp(double value) { // needed now bc the data from OWM isnt always to 1dp, like open-meteo was.
        return Math.round(value * 10.0) / 10.0;
    }

    private String windDirectionMap(Integer windDirection) {
        if (windDirection == null)
            return "N/A";
        String[] directions = { "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW",
                "NW", "NNW" };
        // convert the wind direction in degrees to an index for the directions array
        int index = (int) Math.round(((double) windDirection % 360) / 22.5) % 16;
        return directions[index];
    }

    // had to make this bc i removed open meteo from all bar marine, bc its ip
    // based, and all of my api tokens were shared bc hosting backend on render
    private Integer translateOwmCode(int owmId) {
        if (owmId == 800)
            return 0;
        if (owmId == 801)
            return 1;
        if (owmId == 802)
            return 2;
        if (owmId == 803 || owmId == 804)
            return 3;
        if (owmId >= 700 && owmId < 800)
            return 45;
        if (owmId >= 300 && owmId < 400)
            return 51;
        if (owmId >= 500 && owmId < 600)
            return 61;
        if (owmId >= 200 && owmId < 300)
            return 95;
        if (owmId >= 600 && owmId < 700)
            return 71;
        return 3;
    }

    private String weatherCodeMap(Integer weatherCode) { // using a map
        if (weatherCode == null)
            return "N/A";
        Map<Integer, String> weatherCodes = Map.ofEntries(Map.entry(0, "Clear sky"), Map.entry(1, "Mainly clear"),
                Map.entry(2, "Partly cloudy"), Map.entry(3, "Overcast"), Map.entry(45, "Fog"),
                Map.entry(48, "Depositing rime fog"), Map.entry(51, "Drizzle: Light intensity"),
                Map.entry(53, "Drizzle: Moderate intensity"), Map.entry(55, "Drizzle: Dense intensity"),
                Map.entry(56, "Freezing Drizzle: Light intensity"), Map.entry(57, "Freezing Drizzle: Dense intensity"),
                Map.entry(61, "Rain: Slight intensity"), Map.entry(63, "Rain: Moderate intensity"),
                Map.entry(65, "Rain: Heavy intensity"), Map.entry(66, "Freezing Rain: Light intensity"),
                Map.entry(67, "Freezing Rain: Heavy intensity"), Map.entry(71, "Snow fall: Slight intensity"),
                Map.entry(73, "Snow fall: Moderate intensity"), Map.entry(75, "Snow fall: Heavy intensity"),
                Map.entry(77, "Snow grains"), Map.entry(80, "Rain showers: Slight intensity"),
                Map.entry(81, "Rain showers: Moderate intensity"), Map.entry(82, "Rain showers: Violent intensity"),
                Map.entry(85, "Snow showers: Slight intensity"), Map.entry(86, "Snow showers: Heavy intensity"),
                Map.entry(95, "Thunderstorm: Slight or moderate"), Map.entry(96, "Thunderstorm with slight hail"),
                Map.entry(99, "Thunderstorm with heavy hail"));
        return weatherCodes.getOrDefault(weatherCode, "N/A"); // return the weather code description
    }

    private BeachInfo getBeachData(GeocodingInfo geocodingInfo, String displayName) {
        // chech if the goecoding info is null or empty
        if (geocodingInfo == null || geocodingInfo.results() == null || geocodingInfo.results().isEmpty()) {
            // return the Unknown spot
            return new BeachInfo(1L, "Unknown Spot", 0.0, 0.0, 0.0, 0.0, "N/A", 0.0, 0.0, "N/A",
                    "No Reasoning Available", "N/A", "N/A", "2026-08-07T00:00:00Z", "06:00", "18:00");
        }
        MarineInfo marineInfo = null;
        ForecastInfo forecastInfo = null;
        for (int i = 0; i < 3; i++) {
            boolean marineInfoSuccess = false;
            boolean forecastInfoSuccess = false;
            try {
                marineInfo = restClient.get().uri("https://marine-api.open-meteo.com/v1/marine?latitude="
                        + geocodingInfo.results().get(0).latitude() + "&longitude="
                        + geocodingInfo.results().get(0).longitude()
                        + "&hourly=wave_height,wave_period,wave_direction,sea_surface_temperature&cell_selection=sea")
                        .retrieve().body(MarineInfo.class); // fetch marine stats based off coords
                marineInfoSuccess = true;
            } catch (Exception e) {
                log.warn("Failed to fetch marine data: {}", e.getMessage());
            }
            try {
                forecastInfo = restClient.get()
                        .uri("https://api.openweathermap.org/data/2.5/weather?lat="
                                + geocodingInfo.results().get(0).latitude() + "&lon="
                                + geocodingInfo.results().get(0).longitude() + "&units=metric&appid=" + oWMKey)
                        .retrieve().body(ForecastInfo.class); // fetch forecast info based off coords
                forecastInfoSuccess = true;
            } catch (Exception e) {
                log.warn("Failed to fetch forecast data: {}", e.getMessage());
            }
            if (marineInfoSuccess == true && forecastInfoSuccess == true) {
                break; // if both requests were successful, break out of the loop early
            }
        }
        log.info("marineInfo: {}", marineInfo);
        log.info("forecastInfo: {}", forecastInfo);
        if (marineInfo != null && marineInfo.hourly() != null && marineInfo.hourly().wave_height().isEmpty() == false
                && forecastInfo != null && forecastInfo.main() != null && forecastInfo.wind() != null
                && forecastInfo.sys() != null && forecastInfo.weather() != null
                && forecastInfo.weather().isEmpty() == false) { // boring null checks
            int currentUtcHour = Instant.now().atOffset(ZoneOffset.UTC).getHour();
            // if anoy of the values are 0 retrun the unknown spot
            if (safeDouble(marineInfo.hourly().wave_height(), currentUtcHour) == -1.0
                    || safeDouble(marineInfo.hourly().wave_period(), currentUtcHour) == -1.0
                    || safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour) == -1.0) {
                return new BeachInfo(1L, "Unknown Spot", 0.0, 0.0, 0.0, 0.0, "N/A", 0.0, 0.0, "N/A",
                        "No Reasoning Available", "N/A", "N/A", "2026-08-07T00:00:00Z", "06:00", "18:00");
            }
            return new BeachInfo(null, displayName,
                    ratingCalculator(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour),
                            safeDouble(marineInfo.hourly().wave_period(), currentUtcHour),
                            forecastInfo.wind().speed() * 3.6,
                            safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour),
                            translateOwmCode(forecastInfo.weather().get(0).id()))),
                    round1dp(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour)),
                    round1dp(safeDouble(marineInfo.hourly().wave_period(), currentUtcHour)),
                    round1dp(forecastInfo.wind().speed() * 3.6), windDirectionMap(forecastInfo.wind().deg()), 0.0,
                    round1dp(safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour)),
                    weatherCodeMap(translateOwmCode(forecastInfo.weather().get(0).id())),
                    reasoningWriter(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour),
                            safeDouble(marineInfo.hourly().wave_period(), currentUtcHour),
                            forecastInfo.wind().speed() * 3.6,
                            safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour),
                            translateOwmCode(forecastInfo.weather().get(0).id())),
                    goodStuffWriter(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour),
                            safeDouble(marineInfo.hourly().wave_period(), currentUtcHour),
                            forecastInfo.wind().speed() * 3.6,
                            safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour),
                            translateOwmCode(forecastInfo.weather().get(0).id()))),
                    badStuffWriter(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour),
                            safeDouble(marineInfo.hourly().wave_period(), currentUtcHour),
                            forecastInfo.wind().speed() * 3.6,
                            safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour),
                            translateOwmCode(forecastInfo.weather().get(0).id()))),
                    marineInfo.hourly().time().get(currentUtcHour),
                    Instant.ofEpochSecond(forecastInfo.sys().sunrise()).toString(),
                    Instant.ofEpochSecond(forecastInfo.sys().sunset()).toString());
        }
        return new BeachInfo(1L, "Unknown Spot", 0.0, 0.0, 0.0, 0.0, "N/A", 0.0, 0.0, "N/A", "No Reasoning Available",
                "N/A", "N/A", "2026-08-07T00:00:00Z", "06:00", "18:00");
    }

    private double[] preRatingCalcuator(double wH, double wP, double wS, double wT, Integer weather) {
        double[] scoreArray = new double[5];
        double weatherDouble = weather.doubleValue();
        if (wH < 0.4) { // here i just linearly scale the wave height score, 2.5m being the optimal.
            scoreArray[0] = Math.max(0.0, wH * 5.0); // 0 - 2
        } else if (wH <= 1.0) {
            scoreArray[0] = 2.0 + ((wH - 0.4) / 0.6) * 4.0; // 2 - 6
        } else if (wH <= 2.5) {
            scoreArray[0] = 6.0 + ((wH - 1.0) / 1.5) * 4.0; // 6 - 10
        } else if (wH <= 4.5) {
            scoreArray[0] = 10.0 - ((wH - 2.5) / 2.0) * 2.0; // 10 - 8
        } else {
            scoreArray[0] = Math.max(2.0, 8.0 - (wH - 4.5) * 0.5); // 8 - 2
        }
        if (wP < 6.0) { // here i just linearly scale the wave period score, more than 15s is optimal.
            scoreArray[1] = Math.max(0.0, (wP / 6.0) * 2.0); // 0 - 2
        } else if (wP <= 10.0) {
            scoreArray[1] = 3.0 + ((wP - 6.0) / 4.0) * 3.0; // 3 - 6
        } else if (wP <= 15.0) {
            scoreArray[1] = 6.0 + ((wP - 10.0) / 5.0) * 3.5; // 6 - 9.5
        } else {
            scoreArray[1] = 10.0;
        }
        // here i just linearly scale the wind speed score, less the 9 km/h (open meteo
        // gives it in km/h is optimal, higher wind is worse
        if (wS <= 9.0) {
            scoreArray[2] = 10.0;
        } else if (wS <= 28.0) {
            scoreArray[2] = 10.0 - ((wS - 9.0) / 19.0) * 5.0; // 10 - 5
        } else if (wS <= 46.0) {
            scoreArray[2] = 5.0 - ((wS - 28.0) / 18.0) * 4.0; // 5 - 1
        } else {
            scoreArray[2] = 0.0;
        }
        // here i just linearly scale the water temperature score, 28°C is the optimal
        // temp
        if (wT < 10.0) {
            scoreArray[3] = Math.max(1.0, (wT / 10.0) * 4.0); // 1 - 4
        } else if (wT <= 22.0) {
            scoreArray[3] = 4.0 + ((wT - 10.0) / 12.0) * 6.0; // 4 - 10
        } else if (wT <= 28.0) {
            scoreArray[3] = 10.0;
        } else {
            scoreArray[3] = Math.max(6.0, 10.0 - (wT - 28.0) * 0.5); // 10 - 6
        }
        // here i just take the weathercode and assign it a score based off of it,
        // general trend is that less visibility is worse
        if (weatherDouble >= 0.0 && weatherDouble <= 1.0) {
            scoreArray[4] = 10.0;
        } else if (weatherDouble >= 2.0 && weatherDouble <= 3.0) {
            scoreArray[4] = 7.5;
        } else if (weatherDouble == 45.0 || weatherDouble == 48.0) {
            scoreArray[4] = 4.0;
        } else if (weatherDouble >= 51.0 && weatherDouble <= 67.0) {
            scoreArray[4] = 3.0;
        } else {
            scoreArray[4] = 0.0;
        }
        return scoreArray;
    }

    private double ratingCalculator(double[] scoreArray) {
        // wave height and wave period combo means that wavehight score s now dependent
        // on waveperiod score
        double wHwPCombination = scoreArray[0] * ((scoreArray[1] / 10) * (scoreArray[1] / 10));
        double weightedwHwP = 1.2 * wHwPCombination; // wave height and wave period combination is weighted 1.2 times
        double weightedwS = 0.6 * scoreArray[2]; // wind speed is weighted 0.6 times
        double weightedwT = 0.2 * scoreArray[3]; // water temperature is weighted 0.2 times
        double weightedWeatherScore = 0.1 * scoreArray[4]; // weather score is weighted 0.1 times
        // output the rating rounded to 1dp
        return Math.round(((weightedwHwP + weightedwS + weightedwT + weightedWeatherScore) / 2.1) * 10.0) / 10.0;
    }

    private String reasoningWriter(double wH, double wP, double wS, double wT, Integer weather) {
        double weatherDouble = weather.doubleValue();
        StringBuilder reasoning = new StringBuilder();
        StringJoiner addSpace = new StringJoiner(" ");
        if (wH < 0.4) {
            addSpace.add("Very low wave height.");
        } else if (wH <= 1.0) {
            addSpace.add("Small waves, but still surfable.");
        } else if (wH <= 2.5) {
            addSpace.add("Optimal wave height for surfing.");
        } else if (wH <= 4.5) {
            addSpace.add("Large waves, good for experienced surfers.");
        } else {
            addSpace.add("Extremely large, dangerous waves.");
        }
        if (wP < 6.0) {
            addSpace.add("Short wave period, choppy conditions.");
        } else if (wP <= 10.0) {
            addSpace.add("Moderate wave period, decent conditions.");
        } else if (wP <= 15.0) {
            addSpace.add("Long wave period, well spaced, clean waves.");
        } else {
            addSpace.add("Very long wave period, ideal for surfing.");
        }
        if (wS <= 9.0) {
            addSpace.add("Calm winds, optimal for clean waves.");
        } else if (wS <= 28.0) {
            addSpace.add("Light to moderate winds, minimal impact on wave quality.");
        } else if (wS <= 46.0) {
            addSpace.add("Strong winds, causing choppy conditions, reducing wave quality.");
        } else {
            addSpace.add("Extreme winds, unlikely to be surfable.");
        }
        if (wT < 10.0) {
            addSpace.add("Very cold water, not ideal for surfing.");
        } else if (wT <= 22.0) {
            addSpace.add("Cool to mild water temperature, requires a wetsuit.");
        } else if (wT <= 28.0) {
            addSpace.add("Warm water temperature, optimal for surfing.");
        } else {
            addSpace.add("Very warm water, potentially uncomfortable.");
        }
        if (weatherDouble >= 0.0 && weatherDouble <= 1.0) {
            addSpace.add("Clear skies, excellent visibility.");
        } else if (weatherDouble >= 2.0 && weatherDouble <= 3.0) {
            addSpace.add("Partly cloudy or overcast skies, decent visibility.");
        } else if (weatherDouble == 45.0 || weatherDouble == 48.0) {
            addSpace.add("Foggy conditions severely reduce visibility.");
        } else if (weatherDouble >= 51.0 && weatherDouble <= 67.0) {
            addSpace.add("Rain and drizzle create wet conditions.");
        } else {
            addSpace.add("Severe weather impairs conditions.");
        }
        reasoning.append(addSpace);
        return reasoning.toString();
    }

    private String goodStuffWriter(double[] scoreArray) { // writes the list of good stuff
        String[] features = { "wave height", "wave period", "wind speed", "water temperature", "weather conditions" };
        List<String> goodFeatures = new ArrayList<>();
        for (int i = 0; i < scoreArray.length; i++) {
            if (scoreArray[i] >= 7.0) {
                goodFeatures.add(features[i]);
            }
        }
        if (goodFeatures.isEmpty()) {
            return "No notable positive conditions.";
        }
        if (goodFeatures.size() == 1) { // if only one good feature, return it directly
            return "Good " + goodFeatures.get(0) + ".";
        }
        if (goodFeatures.size() == 2) { // if two good features, return them with and, but no comma
            return "Good " + goodFeatures.get(0) + " and " + goodFeatures.get(1) + ".";
        }
        String allButLast = String.join(", ", goodFeatures.subList(0, goodFeatures.size() - 1));
        String last = goodFeatures.get(goodFeatures.size() - 1);
        return "Good " + allButLast + ", and " + last + "."; // if more than 2, return with commas and and.
    }

    private String badStuffWriter(double[] scoreArray) { // writes the list of bad stuff
        String[] features = { "wave height", "wave period", "wind speed", "water temperature", "weather conditions" };
        List<String> badFeatures = new ArrayList<>();
        for (int i = 0; i < scoreArray.length; i++) {
            if (scoreArray[i] <= 4.0) {
                badFeatures.add(features[i]);
            }
        }
        if (badFeatures.isEmpty()) {
            return "No notable negative conditions.";
        }
        if (badFeatures.size() == 1) {
            return "Suboptimal " + badFeatures.get(0) + ".";
        }
        if (badFeatures.size() == 2) {
            return "Suboptimal " + badFeatures.get(0) + " and " + badFeatures.get(1) + ".";
        }
        String allButLast = String.join(", ", badFeatures.subList(0, badFeatures.size() - 1));
        String last = badFeatures.get(badFeatures.size() - 1);
        return "Suboptimal " + allButLast + ", and " + last + ".";
    }

    public BeachController(SurfCacheService surfCacheService, ObjectMapper objectMapper) {
        this.surfCacheService = surfCacheService;
        this.objectMapper = objectMapper;
    }

    private List<String> generateNameVars(String rawName) {
        List<String> vars = new ArrayList<>();

        // Normalize accents and convert to lowercase
        String cleaned = Normalizer.normalize(rawName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim()
                // remove groups of non alphanumeric chars or apostrophes and swap with a -
                .replaceAll("[^a-z0-9']+", "-")
                // remove any leading or trailing -
                .replaceAll("^-|-$", "");

        String primaryKey = cleaned.contains("beach") ? cleaned : cleaned + "-beach";
        vars.add(primaryKey);

        String[] parts = cleaned.split("-");
        final List<String> suffixes = List.of("beach", "spot", "point", "break", "reef", "surf");

        while (parts.length > 1 && suffixes.contains(parts[parts.length - 1])) {
            parts = Arrays.copyOf(parts, parts.length - 1);
            String strippedKey = String.join("-", parts);
            if (!vars.contains(strippedKey)) {
                vars.add(strippedKey);
            }
        }
        return vars;
    }

    // stats endpoint so I can track cache hits and misses
    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        long hits = cacheHits.get();
        long misses = cacheMisses.get();
        long total = hits + misses;
        double hitRate = total == 0 ? 0.0 : ((double) hits / total) * 100;

        return Map.of(
                "totalRequests", total,
                "cacheHits", hits,
                "cacheMisses", misses,
                "hitRatePercentage", String.format("%.2f%%", hitRate));
    }
}
