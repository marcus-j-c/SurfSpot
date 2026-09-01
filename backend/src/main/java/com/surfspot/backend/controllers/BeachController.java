package com.surfspot.backend.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173") //this allows React to fetch the data from the backend.
@RestController
@RequestMapping("/beaches")
public class BeachController {
    private final RestClient restClient = RestClient.create(); //create a RestClient instance
    private final List<BeachInfo> hardcodedBeaches = List.of( //hardcoded beaches, same as in my db.json on the frontend.
            new BeachInfo(1L, "Unknown Beach", 0.0, 0.0, 0, 0.0, "N/A", 0.0, 0.0, "N/A", "No Reasoning Available", "N/A", "N/A", "2026-08-07T00:00:00Z", "06:00", "18:00"),
            new BeachInfo(2L, "Bonzai Pipeline", 8.6, 3.8, 16, 11.5, "ENE", 0.9, 26.4, "Mostly Sunny", "Large 3.8m swell with a long 16s period combined with light offshore winds produces exceptional, clean barrel conditions.", "Warm 26.4°C water, long 16s period, and clean barrel potential.", "Heavy 3.8m swell presents severe power and shallow reef hazards.", "2026-08-07T08:00:00Z", "06:08", "19:08"),
            new BeachInfo(3L, "Bells Beach", 6.1, 2.3, 14, 18.0, "W", 1.2, 13.8, "Sunny", "Solid swell size and 14s period, but higher wind speeds create surface chop in cool water.", "Sunny weather and solid 2.3m swell height.", "Chilly 13.8°C water temperature and high 18.0-knot winds causing choppy surfaces.", "2026-08-07T10:00:00Z", "07:14", "17:45"),
            new BeachInfo(4L, "Jeffreys Bay", 7.6, 1.6, 12, 14.5, "SW", 1.8, 17.5, "Partly Cloudy", "Fun 1.6m wave height with a 12s period and favorable winds offering clean, lining-up point break sections.", "Favorable SW winds and clean point break shape.", "Relatively modest 1.6m wave height and moderate 14.5-knot wind speed.", "2026-08-07T09:00:00Z", "06:52", "17:42"),
            new BeachInfo(5L, "Teahupo'o", 9.2, 4.5, 17, 9.0, "ESE", 0.4, 27.8, "Scattered Showers", "Heavy 4.5m swell paired with a long 17s period and light winds creates world-class, powerful reef barrels.", "Tropical 27.8°C water, light winds, and powerful long-period swell.", "Scattered rain showers and extremely dangerous heavy waves over shallow reef.", "2026-08-07T08:00:00Z", "06:00", "18:00"),
            new BeachInfo(6L, "Supertubos", 3.5, 2.1, 11, 16.2, "NNW", 2.1, 18.2, "Mostly Sunny", "Decent wave height is hampered by a shorter 11s period, high tide, and strong onshore wind speeds.", "Mostly sunny skies.", "Strong 16.2-knot winds, short 11s period, and high tide fatting out wave sections.", "2026-08-07T08:00:00Z", "06:30", "20:30"),
            new BeachInfo(7L, "Nazaré", 9.1, 8.5, 18, 22.0, "N", 2.4, 16.0, "Overcast", "Massive big-wave swell with an 18s period provides extreme wave energy, despite stronger 22-knot winds.", "Massive big-wave energy and an 18s swell period.", "Strong 22-knot wind speeds, overcast skies, cold water, and high-risk big wave conditions.", "2026-08-07T08:00:00Z", "06:30", "20:30"),
            new BeachInfo(8L, "Uluwatu", 8.2, 2.7, 15, 12.0, "SE", 1.5, 28.5, "Clear", "Solid 2.7m swell with a long 15s period and warm water delivers prime, peeling reef break waves.", "Clear skies, 28.5°C water temp, and clean 15s swell period.", "Moderate 12.0-knot wind speeds and sharp reef hazards.", "2026-08-07T08:00:00Z", "06:15", "18:15"),
            new BeachInfo(9L, "Gold Coast", 8.0, 1.8, 13, 10.5, "SSE", 1.3, 22.1, "Sunny", "Optimal 1.8m wave height, 13s period, and light winds create highly consistent and clean sandbar waves.", "Sunny weather, comfortable water, and manageable light winds.", "Slightly modest 1.8m wave size limits overall wave power.", "2026-08-07T08:00:00Z", "06:30", "17:30"),
            new BeachInfo(10L, "Mavericks", 2.7, 6.2, 16, 15.0, "NW", 1.6, 11.5, "Foggy", "Heavy 6.2m swell energy is severely degraded by low-visibility fog and cold, onshore chop.", "Heavy raw swell height.", "Poor visibility due to fog, cold 11.5°C water, and choppy 15.0-knot winds.", "2026-08-07T08:00:00Z", "06:15", "20:00"),
            new BeachInfo(11L, "Hossegor", 6.9, 2.0, 12, 8.5, "E", 2.8, 20.2, "Sunny", "Light offshore East winds and a clean 2.0m swell yield quality beach break peaks, tempered slightly by high tide.", "Sunny skies, light East offshore winds, and pleasant 20.2°C water.", "High 2.8m tide reducing wave steepness and barrel formation.", "2026-08-07T08:00:00Z", "07:00", "21:30")
    );
    @GetMapping
    public BeachInfo getBeachByName(@RequestParam String name) {
        String cleanedName = Arrays.stream(name.split("-")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" ")); //collect(Collectors.joining(" ")) tells to join with spaces. substring(0,1) grabs first char, as that is 0 up to but not including 1, then substring(1) grabs the rest of the string starting at index 1. And map just applies this capitalisation to each word. This is the same as i did in TS on my frontend.
        for (BeachInfo beach : hardcodedBeaches) { //search for beach.
            if (beach.name().equals(cleanedName)) {
                return beach;
            }
        }
        return hardcodedBeaches.get(0); //grab the unknown beach.
    }

    private GeocodingInfo coordsRequest(String name) {
        String cleanedName = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("-", " ").toLowerCase().trim(); //Normalizer.normalize(name, Normalizer.Form.NFD) split the accent mark and the letter its on into 2 characters replaceAll("\\p{M}", "") vaporises all split off accent marks, replace hyphens with spaces, convert to lowercase, and trim whitespaces.
        GeocodingInfo openMeteoResponse = null;
        for (int i = 0; i < 3; i++) { //try open meteo to 3 times as it could have just been a network error.
            try {
                openMeteoResponse = restClient.get().uri("https://geocoding-api.open-meteo.com/v1/search?name=" + cleanedName).retrieve().body(GeocodingInfo.class); //restClient.get() says go fetch something from the internet, .body(GeocodingInfo.class) puts the result straight into my record.
                break; //if it works get out of the loop early
            }
            catch (Exception e) {} //if it fails, just try again, up to 3 times.
        }
        if (openMeteoResponse != null && openMeteoResponse.results() != null && !openMeteoResponse.results().isEmpty()) { //check if the response isnt empty.
            return openMeteoResponse; // if it isnt return it.
        }
        try { //if the response is empty, try nominatim as my backup.
            BackupGeocodingInfo[] nominatimResponse = restClient.get().uri("https://nominatim.openstreetmap.org/search?q=" + cleanedName + "&format=json").header("User-Agent", "SurfSpot/V1").retrieve().body(BackupGeocodingInfo[].class); //works the same as my open meteo one, just nominatim requires a header
            if (nominatimResponse != null && nominatimResponse.length > 0) { //check if the response isnt empty.
                BackupGeocodingInfo firstResult = nominatimResponse[0];
                GeocodingInfo.BeachCoords coords = new GeocodingInfo.BeachCoords(firstResult.name(), Double.parseDouble(firstResult.lat()), Double.parseDouble(firstResult.lon())); //convert the nominatim response into a geocodinginfo record, damn apis with different formats!!!
                return new GeocodingInfo(List.of(coords)); //return the geocodinginfo record with the coords in a list.
            }
        }
        catch (Exception e) {}
        return null; //if everything fails return null.
    }
    /*@GetMapping("/banzai-pipeline")
    public BeachInfo getBanzaiPipeline() {
        return new BeachInfo(2L, "Bonzai Pipeline", 8.6, 3.8, 16, 11.5, "ENE", 0.9, 26.4, "Mostly Sunny", "Large 3.8m swell with a long 16s period combined with light offshore winds produces exceptional, clean barrel conditions.", "Warm 26.4°C water, long 16s period, and clean barrel potential.", "Heavy 3.8m swell presents severe power and shallow reef hazards.", "2026-08-07T08:00:00Z", "06:08", "19:08");
    }

    @GetMapping("/bells-beach")
    public BeachInfo getBellsBeach() {
        return new BeachInfo(3L, "Bells Beach", 6.1, 2.3, 14, 18.0, "W", 1.2, 13.8, "Sunny", "Solid swell size and 14s period, but higher wind speeds create surface chop in cool water.", "Sunny weather and solid 2.3m swell height.", "Chilly 13.8°C water temperature and high 18.0-knot winds causing choppy surfaces.", "2026-08-07T10:00:00Z", "07:14", "17:45");
    }

    @GetMapping("/jeffreys-bay")
    public BeachInfo getJeffreysBay() {
        return new BeachInfo(4L, "Jeffreys Bay", 7.6, 1.6, 12, 14.5, "SW", 1.8, 17.5, "Partly Cloudy", "Fun 1.6m wave height with a 12s period and favorable winds offering clean, lining-up point break sections.", "Favorable SW winds and clean point break shape.", "Relatively modest 1.6m wave height and moderate 14.5-knot wind speed.", "2026-08-07T09:00:00Z", "06:52", "17:42");
    }*/
}
