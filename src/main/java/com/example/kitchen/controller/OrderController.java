package com.example.kitchen.controller;

import com.example.kitchen.service.OrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
public class OrderController {
    @Autowired
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("get-order-details/{userId}")
    public JSONArray getOrderList(@PathVariable int userId) {
        return orderService.getOrderList(userId);
    }

    @PostMapping("save-order-details")
    public boolean saveOrder(@RequestBody JSONObject jsonObj) {
        return orderService.saveOrder(jsonObj);
    }

}
