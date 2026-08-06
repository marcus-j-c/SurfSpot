package com.surfspot.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beach")
public class BeachController {

    @GetMapping("/banzai-pipeline")
    public BeachInfo getBanzaiPipeline() {
        return new BeachInfo("Banzai Pipeline", "2026-08-07T08:00:00Z", 3.8, 16, 11.5, "ENE", 0.9, 28.0, 26.4, "Mostly Sunny", "06:08", "19:08");
    }
    @GetMapping("/bells-beach")
    public BeachInfo getBellsBeach() {
        return new BeachInfo("Bells Beach", "2026-08-07T10:00:00Z", 2.3, 14, 18.0, "W", 1.2, 15.0, 13.8, "Sunny", "07:14", "17:45");
    }

    @GetMapping("/jeffreys-bay")
    public BeachInfo getJeffreysBay() {
        return new BeachInfo("Jeffreys Bay", "2026-08-07T09:00:00Z", 1.6, 12, 14.5, "SW", 1.8, 19.0, 17.5, "Partly Cloudy", "06:52", "17:42");
    }
}
