package com.surfspot.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaterTempController {

    @GetMapping("/waterTemp")
    public String waterTemp(@RequestParam String water) {
        return "Your request to know " + water + " water temperature has been received.";
    }
}

