package com.example.kitchen.dao;

import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.UserDetails;

import java.util.List;

public interface UserDao {
    boolean saveUserDetails(UserDetails userDetails);


    UserDetails getUserDetailsByEmail(String email);

    boolean addToCart(CartDetails cartDetails);

    CartDetails getCartDetails(int dishId, int userId);

    List<CartDetails> getCartDetailsByUserId(int userId);


    List<CartDetails> getDishIds(int userId);

    boolean deleteItem(int userId, int dishId);

    boolean deleteAllItem(int userId);
}
