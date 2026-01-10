package com.example.kitchen.dao;

import com.example.kitchen.modal.OrderItem;
import com.example.kitchen.modal.OrderedDetails;

import java.util.List;

public interface OrderDao {
    List<OrderedDetails> getAllOrderByUserId(int userId);

    boolean saveOrder(OrderedDetails orderedDetails);

    boolean save(OrderedDetails order);

    boolean saveOrderItems(List<OrderItem> orderItems);

    OrderedDetails findByOrderId(String orderId);

    OrderedDetails findById(int id);

    List<OrderedDetails> findAllByUserId(int userId, int page, int pageSize);

    List<OrderedDetails> findByUserIdAndStatus(int userId, String status, int page, int pageSize);

    long countByUserId(int userId);

    long countByUserIdAndStatus(int userId, String status);

    List<OrderItem> findOrderItemsByOrderId(int orderId);

    boolean updateOrderStatus(int orderId, String status, String cancellationReason);
}
