package com.surfspot.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173") //this allows React to fetch the data from the backend.
@RestController
@RequestMapping("/beaches")
public class BeachController {
    private static final Logger log = LoggerFactory.getLogger(BeachController.class);
    private record GeocodingResult(GeocodingInfo info, String displayName) {}
    private final RestClient restClient = RestClient.create(); //create a RestClient instance
    private final List<BeachInfo> hardcodedBeaches = List.of( //hardcoded beaches, same as in my db.json on the frontend.
            new BeachInfo(1L, "Unknown Spot", 0.0, 0.0, 0.0, 0.0, "N/A", 0.0, 0.0, "N/A", "No Reasoning Available", "N/A", "N/A", "2026-08-07T00:00:00Z", "06:00", "18:00"),
            new BeachInfo(2L, "Bonzai Pipeline", 8.6, 3.8, 16.0, 11.5, "ENE", 0.9, 26.4, "Mostly Sunny", "Large 3.8m swell with a long 16s period combined with light offshore winds produces exceptional, clean barrel conditions.", "Warm 26.4°C water, long 16s period, and clean barrel potential.", "Heavy 3.8m swell presents severe power and shallow reef hazards.", "2026-08-07T08:00:00Z", "06:08", "19:08"),
            new BeachInfo(3L, "Bells Beach", 6.1, 2.3, 14.0, 18.0, "W", 1.2, 13.8, "Sunny", "Solid swell size and 14s period, but higher wind speeds create surface chop in cool water.", "Sunny weather and solid 2.3m swell height.", "Chilly 13.8°C water temperature and high 18.0-knot winds causing choppy surfaces.", "2026-08-07T10:00:00Z", "07:14", "17:45"),
            new BeachInfo(4L, "Jeffreys Bay", 7.6, 1.6, 12.0, 14.5, "SW", 1.8, 17.5, "Partly Cloudy", "Fun 1.6m wave height with a 12s period and favorable winds offering clean, lining-up point break sections.", "Favorable SW winds and clean point break shape.", "Relatively modest 1.6m wave height and moderate 14.5-knot wind speed.", "2026-08-07T09:00:00Z", "06:52", "17:42"),
            new BeachInfo(5L, "Teahupo'o", 9.2, 4.5, 17.0, 9.0, "ESE", 0.4, 27.8, "Scattered Showers", "Heavy 4.5m swell paired with a long 17s period and light winds creates world-class, powerful reef barrels.", "Tropical 27.8°C water, light winds, and powerful long-period swell.", "Scattered rain showers and extremely dangerous heavy waves over shallow reef.", "2026-08-07T08:00:00Z", "06:00", "18:00"),
            new BeachInfo(6L, "Supertubos", 3.5, 2.1, 11.0, 16.2, "NNW", 2.1, 18.2, "Mostly Sunny", "Decent wave height is hampered by a shorter 11s period, high tide, and strong onshore wind speeds.", "Mostly sunny skies.", "Strong 16.2-knot winds, short 11s period, and high tide fatting out wave sections.", "2026-08-07T08:00:00Z", "06:30", "20:30"),
            new BeachInfo(7L, "Nazaré", 9.1, 8.5, 18.0, 22.0, "N", 2.4, 16.0, "Overcast", "Massive big-wave swell with an 18s period provides extreme wave energy, despite stronger 22-knot winds.", "Massive big-wave energy and an 18s swell period.", "Strong 22-knot wind speeds, overcast skies, cold water, and high-risk big wave conditions.", "2026-08-07T08:00:00Z", "06:30", "20:30"),
            new BeachInfo(8L, "Uluwatu", 8.2, 2.7, 15.0, 12.0, "SE", 1.5, 28.5, "Clear", "Solid 2.7m swell with a long 15s period and warm water delivers prime, peeling reef break waves.", "Clear skies, 28.5°C water temp, and clean 15s swell period.", "Moderate 12.0-knot wind speeds and sharp reef hazards.", "2026-08-07T08:00:00Z", "06:15", "18:15"),
            new BeachInfo(9L, "Gold Coast", 8.0, 1.8, 13.0, 10.5, "SSE", 1.3, 22.1, "Sunny", "Optimal 1.8m wave height, 13s period, and light winds create highly consistent and clean sandbar waves.", "Sunny weather, comfortable water, and manageable light winds.", "Slightly modest 1.8m wave size limits overall wave power.", "2026-08-07T08:00:00Z", "06:30", "17:30"),
            new BeachInfo(10L, "Mavericks", 2.7, 6.2, 16.0, 15.0, "NW", 1.6, 11.5, "Foggy", "Heavy 6.2m swell energy is severely degraded by low-visibility fog and cold, onshore chop.", "Heavy raw swell height.", "Poor visibility due to fog, cold 11.5°C water, and choppy 15.0-knot winds.", "2026-08-07T08:00:00Z", "06:15", "20:00"),
            new BeachInfo(11L, "Hossegor", 6.9, 2.0, 12.0, 8.5, "E", 2.8, 20.2, "Sunny", "Light offshore East winds and a clean 2.0m swell yield quality beach break peaks, tempered slightly by high tide.", "Sunny skies, light East offshore winds, and pleasant 20.2°C water.", "High 2.8m tide reducing wave steepness and barrel formation.", "2026-08-07T08:00:00Z", "07:00", "21:30")
    );
    @GetMapping
    public BeachInfo getBeachByName(@RequestParam String name) {
        String cleanedName = Arrays.stream(name.split("-")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" ")); //collect(Collectors.joining(" ")) tells to join with spaces. substring(0,1) grabs first char, as that is 0 up to but not including 1, then substring(1) grabs the rest of the string starting at index 1. And map just applies this capitalisation to each word. This is the same as i did in TS on my frontend.
        for (BeachInfo beach : hardcodedBeaches) { //search for beach.
            if (beach.name().equals(cleanedName)) {
                return beach;
            }
        }
        GeocodingResult res = coordsRequest(name, cleanedName);
        return getBeachData(res.info(), res.displayName()); //if the beach is not in my hardcoded list (temporary list), get it from the apis.
    }

    private GeocodingResult coordsRequest(String name, String displayName) {
        String cleanedName = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("-", " ").toLowerCase().trim(); //Normalizer.normalize(name, Normalizer.Form.NFD) split the accent mark and the letter its on into 2 characters replaceAll("\\p{M}", "") vaporises all split off accent marks, replace hyphens with spaces, convert to lowercase, and trim whitespaces.
        log.info("Searching geocoding API for cleaned name: '{}'", cleanedName);
        GeocodingInfo openMeteoResponse = null;
        for (int i = 0; i < 3; i++) { //try open meteo to 3 times in case of a causing failure network error.
            try {
                openMeteoResponse = restClient.get().uri("https://geocoding-api.open-meteo.com/v1/search?name=" + cleanedName).retrieve().body(GeocodingInfo.class); //restClient.get() says go fetch something from the internet, .body(GeocodingInfo.class) puts the result straight into my record.
                break; //if it works get out of the loop early
            }
            catch (Exception e) {//if it fails, just try again, up to 3 times.
                log.warn("Open-Meteo request failed on attempt {}: {}", i + 1, e.getMessage());
            }
        }
        log.info("Open-Meteo geocoding response: {}", openMeteoResponse);
        if (openMeteoResponse != null && openMeteoResponse.results() != null && !openMeteoResponse.results().isEmpty()) { //check if the response isnt empty.
            ArrayList<GeocodingInfo.BeachCoords> sortOpenMeteoResponse = new ArrayList<>(openMeteoResponse.results());
            sortOpenMeteoResponse.sort(Comparator.comparing(GeocodingInfo.BeachCoords::population, Comparator.nullsLast(Comparator.reverseOrder()))); //sort openMeteoResponse by population from highest to lowest nulls at the very bottom.
            return new GeocodingResult(new GeocodingInfo(sortOpenMeteoResponse), displayName); // if it isnt return it.
        }
        log.info("No Open-Meteo results, falling back to Nominatim for '{}'", cleanedName);
        try { //if the response is empty, try nominatim as my backup.
            BackupGeocodingInfo[] nominatimResponse = restClient.get().uri("https://nominatim.openstreetmap.org/search?q=" + cleanedName + "&format=json").header("User-Agent", "SurfSpot/V1").retrieve().body(BackupGeocodingInfo[].class); //works the same as my open meteo one, just nominatim requires a header
            if (nominatimResponse != null && nominatimResponse.length > 0) { //check if the response isnt empty.
                Arrays.sort(nominatimResponse, Comparator.comparingDouble(BackupGeocodingInfo::importance).reversed()); //sort nominatim respone by importance, from highest to lowest,
                BackupGeocodingInfo firstResult = nominatimResponse[0]; //temporary before dropdown box on frontend search bar
                GeocodingInfo.BeachCoords coords = new GeocodingInfo.BeachCoords(firstResult.name(), Double.parseDouble(firstResult.lat()), Double.parseDouble(firstResult.lon()), 0); //convert the nominatim response into a geocodinginfo record, damn apis with different formats!!!
                return new GeocodingResult(new GeocodingInfo(List.of(coords)), displayName); //return the geocodinginfo record with the coords in a list.
            }
        }
        catch (Exception e) {
            log.warn("Nominatim lookup failed: {}", e.getMessage());
        }
        final String [] suffixes = {"beach", "spot", "point", "break", "reef", "surf"};
        String [] doubleCleanedName = cleanedName.split(" ");
        for (String suffix: suffixes) {
            if (doubleCleanedName[doubleCleanedName.length - 1].equalsIgnoreCase(suffix)) {
                String cleanedNameWithoutSuffix = String.join(" ", Arrays.copyOf(doubleCleanedName, doubleCleanedName.length - 1)); //remove the suffix from the name
                log.info("Searching geocoding API for cleaned name without suffix: '{}'", cleanedNameWithoutSuffix);
                String newDisplayName = Arrays.stream(cleanedNameWithoutSuffix.split(" ")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
                return coordsRequest(cleanedNameWithoutSuffix, newDisplayName); //recursively call coordsRequest with the cleaned name without the suffix
            }
        }
        log.info("No results found for '{}', returning null", cleanedName);
        return new GeocodingResult(null, displayName); //if all else fails, return null
    }

    private double safeDouble(List<Double> list, int currentUtcHour) {
        return (list != null && list.size() > currentUtcHour && list.get(currentUtcHour) != null) ? list.get(currentUtcHour) : -1.0; //if the list doesnt exist, is empty or the first element is null, return -1.0, otherwise return the first element.
    }

    private String windDirectionMap(Integer windDirection) {
        if (windDirection == null) return "N/A";
        String[] directions = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(((double) windDirection % 360) / 22.5) % 16; //convert the wind direction in degrees to an index for the directions array
        return directions[index];
    }
    private String weatherCodeMap(Integer weatherCode) { //using a map
        if (weatherCode == null) return "N/A";
        Map<Integer, String> weatherCodes = Map.ofEntries(Map.entry(0,"Clear sky"),Map.entry(1,"Mainly clear"),Map.entry(2,"Partly cloudy"),Map.entry(3,"Overcast"),Map.entry(45,"Fog"),Map.entry(48,"Depositing rime fog"),Map.entry(51,"Drizzle: Light intensity"),Map.entry(53,"Drizzle: Moderate intensity"),Map.entry(55,"Drizzle: Dense intensity"),Map.entry(56,"Freezing Drizzle: Light intensity"),Map.entry(57,"Freezing Drizzle: Dense intensity"),Map.entry(61,"Rain: Slight intensity"),Map.entry(63,"Rain: Moderate intensity"),Map.entry(65,"Rain: Heavy intensity"),Map.entry(66,"Freezing Rain: Light intensity"),Map.entry(67,"Freezing Rain: Heavy intensity"),Map.entry(71,"Snow fall: Slight intensity"),Map.entry(73,"Snow fall: Moderate intensity"),Map.entry(75,"Snow fall: Heavy intensity"),Map.entry(77,"Snow grains"),Map.entry(80,"Rain showers: Slight intensity"),Map.entry(81,"Rain showers: Moderate intensity"),Map.entry(82,"Rain showers: Violent intensity"),Map.entry(85,"Snow showers: Slight intensity"),Map.entry(86,"Snow showers: Heavy intensity"),Map.entry(95,"Thunderstorm: Slight or moderate"),Map.entry(96,"Thunderstorm with slight hail"),Map.entry(99,"Thunderstorm with heavy hail"));
        return weatherCodes.getOrDefault(weatherCode, "N/A"); //return the weather code description
    }

    private BeachInfo getBeachData(GeocodingInfo geocodingInfo, String displayName) {
        if (geocodingInfo == null || geocodingInfo.results() == null || geocodingInfo.results().isEmpty()) { //check if the geocoding info is null or empty
            return hardcodedBeaches.get(0); //return the unknown spot.
        }
        MarineInfo marineInfo = null;
        ForecastInfo forecastInfo = null;
        for (int i = 0; i < 3; i++) {
            boolean marineInfoSuccess = false;
            boolean forecastInfoSuccess = false;
            try {
                marineInfo = restClient.get().uri("https://marine-api.open-meteo.com/v1/marine?latitude=" + geocodingInfo.results().get(0).latitude() + "&longitude=" + geocodingInfo.results().get(0).longitude() + "&hourly=wave_height,wave_period,wave_direction,sea_surface_temperature&cell_selection=sea").retrieve().body(MarineInfo.class); //fetch marine stats based off coords
                marineInfoSuccess = true;
            }
            catch (Exception e) {
                log.warn("Failed to fetch marine data: {}", e.getMessage());
            }
            try {
                forecastInfo = restClient.get().uri("https://api.open-meteo.com/v1/forecast?latitude=" + geocodingInfo.results().get(0).latitude() + "&longitude=" + geocodingInfo.results().get(0).longitude() + "&hourly=temperature_2m,wind_speed_10m,wind_direction_10m,weathercode&daily=sunrise,sunset").retrieve().body(ForecastInfo.class); //fetch forecast info based off coords
                forecastInfoSuccess = true;
            }
            catch (Exception e) {
                log.warn("Failed to fetch forecast data: {}", e.getMessage());
            }
            if (marineInfoSuccess == true && forecastInfoSuccess == true) {
                break; //if both requests were successful, break out of the loop early
            }
        }
        log.info("marineInfo: {}", marineInfo);
        log.info("forecastInfo: {}", forecastInfo);
        if (marineInfo != null && marineInfo.hourly() != null && marineInfo.hourly().wave_height().isEmpty() == false && forecastInfo != null && forecastInfo.hourly() != null && forecastInfo.hourly().temperature_2m().isEmpty() == false && forecastInfo.daily() != null && forecastInfo.daily().sunset().isEmpty() == false) { //boring null checks
            int currentUtcHour = Instant.now().atOffset(ZoneOffset.UTC).getHour();
            if (safeDouble(marineInfo.hourly().wave_height(), currentUtcHour) == -1.0 || safeDouble(marineInfo.hourly().wave_period(), currentUtcHour) == -1.0 || safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour) == -1.0 || safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour) == -1.0) { //if any of the values are 0, return the unknown spot.
                return hardcodedBeaches.get(0);
            }
            return new BeachInfo(null, displayName, ratingCalculator(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour), safeDouble(marineInfo.hourly().wave_period(), currentUtcHour), safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour), safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour), forecastInfo.hourly().weathercode().get(currentUtcHour))), safeDouble(marineInfo.hourly().wave_height(), currentUtcHour), safeDouble(marineInfo.hourly().wave_period(), currentUtcHour), safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour), windDirectionMap(forecastInfo.hourly().wind_direction_10m().get(currentUtcHour)), 0.0, safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour), weatherCodeMap(forecastInfo.hourly().weathercode().get(currentUtcHour)), reasoningWriter(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour), safeDouble(marineInfo.hourly().wave_period(), currentUtcHour), safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour), safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour), forecastInfo.hourly().weathercode().get(currentUtcHour)), goodStuffWriter(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour), safeDouble(marineInfo.hourly().wave_period(), currentUtcHour), safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour), safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour), forecastInfo.hourly().weathercode().get(currentUtcHour))), badStuffWriter(preRatingCalcuator(safeDouble(marineInfo.hourly().wave_height(), currentUtcHour), safeDouble(marineInfo.hourly().wave_period(), currentUtcHour), safeDouble(forecastInfo.hourly().wind_speed_10m(), currentUtcHour), safeDouble(marineInfo.hourly().sea_surface_temperature(), currentUtcHour), forecastInfo.hourly().weathercode().get(currentUtcHour))), marineInfo.hourly().time().get(currentUtcHour), forecastInfo.daily().sunrise().get(0), forecastInfo.daily().sunset().get(0));
        }
        return hardcodedBeaches.get(0);
    }

    private double [] preRatingCalcuator (double wH, double wP, double wS, double wT, Integer weather) {
        double [] scoreArray = new double [5];
        double weatherDouble = weather.doubleValue();
        if (wH < 0.4) { //here i just linearly scale the wave height score, 2.5m being the optimal.
            scoreArray[0] = Math.max(0.0, wH * 5.0); // 0 - 2
        }
        else if (wH <= 1.0) {
            scoreArray[0] = 2.0 + ((wH - 0.4) / 0.6) * 4.0; // 2 - 6
        }
        else if (wH <= 2.5) {
            scoreArray[0] = 6.0 + ((wH - 1.0) / 1.5) * 4.0; // 6 - 10
        }
        else if (wH <= 4.5) {
            scoreArray[0] = 10.0 - ((wH - 2.5) / 2.0) * 2.0; // 10 - 8
        }
        else {
            scoreArray[0] = Math.max(2.0, 8.0 - (wH - 4.5) * 0.5); // 8 - 2
        }
        if (wP < 6.0) { //here i just linearly scale the wave period score, more than 15s is optimal.
            scoreArray[1] = Math.max(0.0, (wP / 6.0) * 2.0); // 0 - 2
        }
        else if (wP <= 10.0) {
            scoreArray[1] = 3.0 + ((wP - 6.0) / 4.0) * 3.0; // 3 - 6
        }
        else if (wP <= 15.0) {
            scoreArray[1] = 6.0 + ((wP - 10.0) / 5.0) * 3.5; // 6 - 9.5
        }
        else {
            scoreArray[1] = 10.0;
        }
        if (wS <= 9.0) { //here i just linearly scale the wind speed score, less than 9 km/h (open meteo gives in in km/) is optimal. Higher wind is worse.
            scoreArray[2] = 10.0;
        }
        else if (wS <= 28.0) {
            scoreArray[2] = 10.0 - ((wS - 9.0) / 19.0) * 5.0; // 10 - 5
        }
        else if (wS <= 46.0) {
            scoreArray[2] = 5.0 - ((wS - 28.0) / 18.0) * 4.0; // 5 - 1
        }
        else {
            scoreArray[2] = 0.0;
        }
        if (wT < 10.0) { //here i just linearly scale the water temperature score, 28°C is the optimal temp.
            scoreArray[3] = Math.max(1.0, (wT / 10.0) * 4.0); // 1 - 4
        }
        else if (wT <= 22.0) {
            scoreArray[3] = 4.0 + ((wT - 10.0) / 12.0) * 6.0; // 4 - 10
        }
        else if (wT <= 28.0) {
            scoreArray[3] = 10.0;
        }
        else {
            scoreArray[3] = Math.max(6.0, 10.0 - (wT - 28.0) * 0.5); // 10 - 6
        }
        if (weatherDouble >= 0.0 && weatherDouble <= 1.0) { //here i just tke the weathercode, and assign a score based off of it, general trend is that less visibility is worse.
            scoreArray[4] = 10.0;
        }
        else if (weatherDouble >= 2.0 && weatherDouble <= 3.0) {
            scoreArray[4] = 7.5;
        }
        else if (weatherDouble == 45.0 || weatherDouble == 48.0) {
            scoreArray[4] = 4.0;
        }
        else if (weatherDouble >= 51.0 && weatherDouble <= 67.0) {
            scoreArray[4] = 3.0;
        }
        else {
            scoreArray[4] = 0.0;
        }
        return scoreArray;
    }

    private double ratingCalculator(double [] scoreArray) {
        double wHwPCombination = scoreArray[0] * ((scoreArray[1] / 10) * (scoreArray[1] / 10)); //wave height and wave period combination, means that waveHeight score is now dependent on wavePeriod score.
        double weightedwHwP = 1.2 * wHwPCombination; //wave height and wave period combination is weighted 1.2 times
        double weightedwS = 0.6 * scoreArray[2]; //wind speed is weighted 0.6 times
        double weightedwT = 0.2 * scoreArray[3]; //water temperature is weighted 0.2 times
        double weightedWeatherScore = 0.1 * scoreArray[4]; //weather score is weighted 0.1 times
        return Math.round(((weightedwHwP + weightedwS + weightedwT + weightedWeatherScore) / 2.1) * 10.0) / 10.0; //output the rating rounded to 1dp.
    }

    private String reasoningWriter(double wH, double wP, double wS, double wT, Integer weather) {
        double weatherDouble = weather.doubleValue();
        StringBuilder reasoning = new StringBuilder();
        StringJoiner addSpace = new StringJoiner(" ");
        if (wH < 0.4) {
            addSpace.add("Very low wave height.");
        }
        else if (wH <= 1.0) {
            addSpace.add("Small waves, but still surfable.");
        }
        else if (wH <= 2.5) {
            addSpace.add("Optimal wave height for surfing.");
        }
        else if (wH <= 4.5) {
            addSpace.add("Large waves, good for experienced surfers.");
        }
        else {
            addSpace.add("Extremely large, dangerous waves.");
        }
        if (wP < 6.0) {
            addSpace.add("Short wave period, choppy conditions.");
        }
        else if (wP <= 10.0) {
            addSpace.add("Moderate wave period, decent conditions.");
        }
        else if (wP <= 15.0) {
            addSpace.add("Long wave period, well spaced, clean waves.");
        }
        else {
            addSpace.add("Very long wave period, ideal for surfing.");
        }
        if (wS <= 9.0) {
            addSpace.add("Calm winds, optimal for clean waves.");
        }
        else if (wS <= 28.0) {
            addSpace.add("Light to moderate winds, minimal impact on wave quality.");
        }
        else if (wS <= 46.0) {
            addSpace.add("Strong winds, causing choppy conditions, reducing wave quality.");
        }
        else {
            addSpace.add("Extreme winds, unlikely to be surfable.");
        }
        if (wT < 10.0) {
            addSpace.add("Very cold water, not ideal for surfing.");
        }
        else if (wT <= 22.0) {
            addSpace.add("Cool to mild water temperature, requires a wetsuit.");
        }
        else if (wT <= 28.0) {
            addSpace.add("Warm water temperature, optimal for surfing.");
        }
        else {
            addSpace.add("Very warm water, potentially uncomfortable.");
        }
        if (weatherDouble >= 0.0 && weatherDouble <= 1.0) {
            addSpace.add("Clear skies, excellent visibility.");
        }
        else if (weatherDouble >= 2.0 && weatherDouble <= 3.0) {
            addSpace.add("Partly cloudy or overcast skies, decent visibility.");
        }
        else if (weatherDouble == 45.0 || weatherDouble == 48.0) {
            addSpace.add("Foggy conditions severely reduce visibility.");
        }
        else if (weatherDouble >= 51.0 && weatherDouble <= 67.0) {
            addSpace.add("Rain and drizzle create wet conditions.");
        }
        else {
            addSpace.add("Severe weather impairs conditions.");
        }
        reasoning.append(addSpace);
        return reasoning.toString();
    }
    private String goodStuffWriter(double[] scoreArray) { //writes the list of good stuff
        String[] features = {"wave height", "wave period", "wind speed", "water temperature", "weather conditions"};
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

    private String badStuffWriter(double[] scoreArray) { //writes the list of bad stuff
        String[] features = {"wave height", "wave period", "wind speed", "water temperature", "weather conditions"};
        List<String> badFeatures = new ArrayList<>();
        for (int i = 0; i < scoreArray.length; i++) {
            if (scoreArray[i] <= 7.0) {
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
}
