package com.example.kitchen.dao;

import com.example.kitchen.modal.CartDetails;

import java.util.List;

public interface CartDao {
    CartDetails findByUserIdAndDishId(int userId, int dishId);

    List<CartDetails> findAllByUserId(int userId);

    boolean save(CartDetails cartDetails);

    boolean deleteByUserIdAndDishId(int userId, int dishId);

    boolean deleteAllByUserId(int userId);
}
