package com.example.kitchen.controller;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("noauth/monitor")
public class HealthCheckController {

    @GetMapping("/health-check")
    public JSONObject getHealthStatus() {
        return new JSONObject().element("status", true);
    }

}
