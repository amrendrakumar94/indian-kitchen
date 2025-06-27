package com.example.kitchen.controller;

import com.example.kitchen.service.ReservationService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping("/get-reservation-details")
    public JSONObject getReservationDetails(@RequestBody JSONObject jsonObj) {
        return reservationService.getReservationDetails(jsonObj);
    }
}
