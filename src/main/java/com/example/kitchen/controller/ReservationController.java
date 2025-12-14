package com.example.kitchen.controller;

import com.example.kitchen.service.ReservationService;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/get-reservation-details")
    public JSONObject getReservationDetails(@RequestBody JSONObject jsonObj) {
        return reservationService.getReservationDetails(jsonObj);
    }
}
