package com.example.kitchen.controller;

import com.example.kitchen.service.OrderService;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("get-order-details/{userId}")
    public JSONArray getOrderList(@PathVariable int userId) {
        return orderService.getOrderList(userId);
    }

    @PostMapping("save-order-details")
    public boolean saveOrder(@RequestBody JSONObject jsonObj) {
        return orderService.saveOrder(jsonObj);
    }

}
