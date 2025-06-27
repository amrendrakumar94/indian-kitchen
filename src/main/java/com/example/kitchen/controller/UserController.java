package com.example.kitchen.controller;

import com.example.kitchen.dto.CartDetailDto;
import com.example.kitchen.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in-user")
    public JSONObject userSignUp(@RequestBody JSONObject jsonObject) {
        return userService.signUpUser(jsonObject);
    }

    @PostMapping("/user-login")
    public JSONObject userLogin(@RequestBody JSONObject jsonObject) {
        return userService.userLogin(jsonObject);
    }

    @PostMapping("/add-to-cart")
    public List<Integer> addToCart(@RequestBody JSONObject jsonObject) {
        return userService.addToCart(jsonObject);
    }

    @GetMapping("/get-dish-details-by-user-id/{userId}")
    public List<CartDetailDto> getDishDetailsLisByForCart(@PathVariable int userId) {
        return userService.getDishDetailsLisByForCart(userId);
    }

    @DeleteMapping("/remove-cart-item/{userId}/{dishId}")
    public boolean deleteItem(@PathVariable int userId, @PathVariable int dishId) {
        return userService.deleteItem(userId, dishId);
    }

    @PostMapping("/increment-cart-item/{userId}/{dishId}")
    public boolean itemIncrement(@PathVariable int userId, @PathVariable int dishId) {
        return userService.incrementItem(userId, dishId);
    }

    @PostMapping("/decrement-cart-item/{userId}/{dishId}")
    public boolean itemDecrement(@PathVariable int userId, @PathVariable int dishId) {
        return userService.decrementItem(userId, dishId);
    }

    @DeleteMapping("/order-cart-item/{userId}")
    public boolean orderItems(@PathVariable int userId) {
        return userService.orderItems(userId);
    }


}
