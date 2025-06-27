package com.example.kitchen.dao;

import com.example.kitchen.modal.OrderedDetails;

import java.util.List;

public interface OrderDao {
    List<OrderedDetails> getAllOrderByUserId(int userId);

    boolean saveOrder(OrderedDetails orderedDetails);
}
