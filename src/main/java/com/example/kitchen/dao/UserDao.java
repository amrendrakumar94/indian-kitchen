package com.example.kitchen.dao;

import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean saveOrUpdateUser(User user);


    Optional<User> getUserByPhone(String phoneNumber);

    boolean addToCart(CartDetails cartDetails);

    CartDetails getCartDetails(int dishId, int userId);

    List<CartDetails> getCartDetailsByUserId(int userId);


    List<CartDetails> getDishIds(int userId);

    boolean deleteItem(int userId, int dishId);

    boolean deleteAllItem(int userId);
}
