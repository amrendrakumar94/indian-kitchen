package com.example.kitchen.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kitchen.dto.CartDetailDto;
import com.example.kitchen.service.UserService;

import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    @PostMapping("add-to-cart")
    public List<Integer> addToCart(@RequestBody JSONObject jsonObject) {
        return userService.addToCart(jsonObject);
    }

    @GetMapping("get-dish-details-by-user-id/{userId}")
    public List<CartDetailDto> getDishDetailsLisByForCart(@PathVariable int userId) {
        return userService.getDishDetailsLisByForCart(userId);
    }

    @DeleteMapping("remove-cart-item/{userId}/{dishId}")
    public boolean deleteItem(@PathVariable int userId, @PathVariable int dishId) {
        return userService.deleteItem(userId, dishId);
    }

    @PostMapping("increment-cart-item/{userId}/{dishId}")
    public boolean itemIncrement(@PathVariable int userId, @PathVariable int dishId) {
        return userService.incrementItem(userId, dishId);
    }

    @PostMapping("decrement-cart-item/{userId}/{dishId}")
    public boolean itemDecrement(@PathVariable int userId, @PathVariable int dishId) {
        return userService.decrementItem(userId, dishId);
    }

    @DeleteMapping("order-cart-item/{userId}")
    public boolean orderItems(@PathVariable int userId) {
        return userService.orderItems(userId);
    }

}
