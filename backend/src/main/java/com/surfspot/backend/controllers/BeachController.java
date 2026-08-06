package com.surfspot.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beach")
public class BeachController {

    @GetMapping("/BanzaiPipeline")
    public Beach getBanzaiPipeline() {
        Beach banzaiPipeline = new Beach("Banzai Pipeline", 14);
        return banzaiPipeline;
    }
    @GetMapping("/BellsBeach")
    public Beach getBellsBeach() {
        Beach bellsBeach = new Beach("Bells Beach", 20);
        return bellsBeach;
    }

    @GetMapping("/JeffreysBay")
    public BeachInfo getJeffreysBay() {
        BeachInfo jeffreysBay = new BeachInfo("Jeffreys Bay", 24);
        return jeffreysBay;
    }
}
