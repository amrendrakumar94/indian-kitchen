package com.example.kitchen.service;

import com.example.kitchen.dao.CartDao;
import com.example.kitchen.dao.OrderDao;
import com.example.kitchen.dto.*;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.OrderItem;
import com.example.kitchen.modal.OrderedDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderManagementService {

    private final OrderDao orderDao;
    private final CartDao cartDao;
    private final CartService cartService;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.09"); // 9%
    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("40");
    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("500");

    @Transactional
    public OrderResponseDto placeOrder(int userId, PlaceOrderRequest request) {
        try {
            // Validate delivery address
            if (request.getDeliveryAddress() == null) {
                throw new IllegalArgumentException("Delivery address is required");
            }
            if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
                throw new IllegalArgumentException("Payment method is required");
            }

            // Get cart items
            List<CartDetails> cartItems = cartDao.findAllByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                throw new IllegalArgumentException("Cart is empty");
            }

            // Calculate order summary
            CartSummaryDto cartSummary = cartService.calculateSummary(userId);

            // Generate order ID
            String orderId = generateOrderId();
            String orderNumber = orderId.substring(orderId.lastIndexOf("-") + 1);

            // Create order
            OrderedDetails order = new OrderedDetails();
            order.setOrderId(orderId);
            order.setOrderNumber(orderNumber);
            order.setUserId(userId);
            order.setStatus("pending");
            order.setSubtotal(cartSummary.getSubtotal());
            order.setTax(cartSummary.getTax());
            order.setDeliveryCharge(cartSummary.getDeliveryCharge());
            order.setDiscount(cartSummary.getDiscount());
            order.setTotal(cartSummary.getTotal());
            order.setPaymentMethod(request.getPaymentMethod());
            order.setPaymentStatus(request.getPaymentMethod().equalsIgnoreCase("COD") ? "pending" : "pending");

            // Set delivery address
            DeliveryAddressDto address = request.getDeliveryAddress();
            order.setDeliveryStreet(address.getStreet());
            order.setDeliveryCity(address.getCity());
            order.setDeliveryState(address.getState());
            order.setDeliveryZipCode(address.getZipCode());
            order.setDeliveryPhone(address.getPhone());

            order.setSpecialInstructions(request.getSpecialInstructions());

            // Set estimated delivery (1 hour from now)
            order.setEstimatedDelivery(LocalDateTime.now().plusHours(1));

            // Save order
            boolean orderSaved = orderDao.save(order);
            if (!orderSaved) {
                throw new RuntimeException("Failed to save order");
            }

            // Get the saved order to get the generated ID
            OrderedDetails savedOrder = orderDao.findByOrderId(orderId);
            if (savedOrder == null) {
                throw new RuntimeException("Failed to retrieve saved order");
            }

            // Create order items from cart
            List<OrderItem> orderItems = new ArrayList<>();
            List<OrderItemDto> orderItemDtos = new ArrayList<>();

            for (CartDetails cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(savedOrder.getId());
                orderItem.setProductId(cartItem.getDishId());
                orderItem.setDishId(cartItem.getDishId());
                orderItem.setDishName("Dish " + cartItem.getDishId()); // Will be populated from dish details
                orderItem.setProductName("Product " + cartItem.getDishId()); // Will be populated from product details
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());
                // Calculate subtotal = price * quantity
                BigDecimal subtotal = cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
                orderItem.setSubtotal(subtotal);
                orderItem.setImage(""); // Will be populated from product details
                orderItems.add(orderItem);

                // Create DTO
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductId(cartItem.getDishId());
                itemDto.setProductName("Product " + cartItem.getDishId());
                itemDto.setQuantity(cartItem.getQuantity());
                itemDto.setPrice(cartItem.getPrice());
                itemDto.setImage("");
                orderItemDtos.add(itemDto);
            }

            // Save order items
            boolean itemsSaved = orderDao.saveOrderItems(orderItems);
            if (!itemsSaved) {
                throw new RuntimeException("Failed to save order items");
            }

            // Clear cart
            cartDao.deleteAllByUserId(userId);

            // Build response
            OrderResponseDto response = new OrderResponseDto();
            response.setOrderId(orderId);
            response.setOrderNumber(orderNumber);
            response.setOrderDate(savedOrder.getOrderDate());
            response.setEstimatedDelivery(savedOrder.getEstimatedDelivery());
            response.setStatus(savedOrder.getStatus());
            response.setItems(orderItemDtos);

            OrderSummaryDto summary = new OrderSummaryDto();
            summary.setSubtotal(savedOrder.getSubtotal());
            summary.setTax(savedOrder.getTax());
            summary.setDeliveryCharge(savedOrder.getDeliveryCharge());
            summary.setDiscount(savedOrder.getDiscount());
            summary.setTotal(savedOrder.getTotal());
            response.setSummary(summary);

            return response;

        } catch (Exception e) {
            log.error("Error in placeOrder(). userId: {}", userId, e);
            throw e;
        }
    }

    public OrderHistoryResponseDto getOrders(int userId, Integer page, Integer pageSize, String status) {
        try {
            int currentPage = page != null ? page : 1;
            int size = pageSize != null ? pageSize : 10;

            List<OrderedDetails> orders;
            long totalCount;

            if (status != null && !status.isEmpty()) {
                orders = orderDao.findByUserIdAndStatus(userId, status, currentPage, size);
                totalCount = orderDao.countByUserIdAndStatus(userId, status);
            } else {
                orders = orderDao.findAllByUserId(userId, currentPage, size);
                totalCount = orderDao.countByUserId(userId);
            }

            List<OrderHistoryDto> orderHistoryList = new ArrayList<>();
            for (OrderedDetails order : orders) {
                OrderHistoryDto historyDto = new OrderHistoryDto();
                historyDto.setOrderId(order.getOrderId());
                historyDto.setOrderNumber(order.getOrderId().substring(order.getOrderId().lastIndexOf("-") + 1));
                historyDto.setOrderDate(order.getOrderDate());
                historyDto.setStatus(order.getStatus());
                historyDto.setTotal(order.getTotal());

                // Get order items
                List<OrderItem> orderItems = orderDao.findOrderItemsByOrderId(order.getId());
                historyDto.setItemCount(orderItems.size());

                List<OrderItemDto> itemDtos = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    itemDto.setImage(item.getImage());
                    itemDtos.add(itemDto);
                }
                historyDto.setItems(itemDtos);

                orderHistoryList.add(historyDto);
            }

            // Build pagination
            int totalPages = (int) Math.ceil((double) totalCount / size);
            PaginationDto pagination = new PaginationDto();
            pagination.setCurrentPage(currentPage);
            pagination.setPageSize(size);
            pagination.setTotalPages(totalPages);
            pagination.setTotalItems((long) totalCount);
            pagination.setHasNextPage(currentPage < totalPages);
            pagination.setHasPreviousPage(currentPage > 1);

            OrderHistoryResponseDto response = new OrderHistoryResponseDto();
            response.setOrders(orderHistoryList);
            response.setPagination(pagination);

            return response;

        } catch (Exception e) {
            log.error("Error in getOrders(). userId: {}", userId, e);
            throw e;
        }
    }

    public OrderDetailsDto getOrderDetails(int userId, String orderId) {
        try {
            OrderedDetails order = orderDao.findByOrderId(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found");
            }
            if (order.getUserId() != userId) {
                throw new IllegalArgumentException("Unauthorized access to order");
            }

            OrderDetailsDto details = new OrderDetailsDto();
            details.setOrderId(order.getOrderId());
            details.setOrderNumber(order.getOrderId().substring(order.getOrderId().lastIndexOf("-") + 1));
            details.setOrderDate(order.getOrderDate());
            details.setStatus(order.getStatus());
            details.setPaymentMethod(order.getPaymentMethod());
            details.setPaymentStatus(order.getPaymentStatus());
            details.setSpecialInstructions(order.getSpecialInstructions());

            // Status history (simplified - in real app, would have separate table)
            List<OrderStatusHistoryDto> statusHistory = new ArrayList<>();
            OrderStatusHistoryDto placedStatus = new OrderStatusHistoryDto();
            placedStatus.setStatus("pending");
            placedStatus.setTimestamp(order.getOrderDate());
            placedStatus.setNote("Order placed");
            statusHistory.add(placedStatus);

            if (order.getStatus().equals("delivered") || order.getStatus().equals("cancelled")) {
                OrderStatusHistoryDto finalStatus = new OrderStatusHistoryDto();
                finalStatus.setStatus(order.getStatus());
                finalStatus.setTimestamp(LocalDateTime.now()); // Would be actual timestamp
                finalStatus.setNote(order.getStatus().equals("delivered") ? "Order delivered" : "Order cancelled");
                statusHistory.add(finalStatus);
            }
            details.setStatusHistory(statusHistory);

            // Order items
            List<OrderItem> orderItems = orderDao.findOrderItemsByOrderId(order.getId());
            List<OrderItemDto> itemDtos = new ArrayList<>();
            for (OrderItem item : orderItems) {
                OrderItemDto itemDto = new OrderItemDto();
                itemDto.setProductId(item.getProductId());
                itemDto.setProductName(item.getProductName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setPrice(item.getPrice());
                itemDto.setImage(item.getImage());
                itemDtos.add(itemDto);
            }
            details.setItems(itemDtos);

            // Summary
            OrderSummaryDto summary = new OrderSummaryDto();
            summary.setSubtotal(order.getSubtotal());
            summary.setTax(order.getTax());
            summary.setDeliveryCharge(order.getDeliveryCharge());
            summary.setDiscount(order.getDiscount());
            summary.setTotal(order.getTotal());
            details.setSummary(summary);

            // Delivery address
            DeliveryAddressDto address = new DeliveryAddressDto();
            address.setStreet(order.getDeliveryStreet());
            address.setCity(order.getDeliveryCity());
            address.setState(order.getDeliveryState());
            address.setZipCode(order.getDeliveryZipCode());
            address.setPhone(order.getDeliveryPhone());
            details.setDeliveryAddress(address);

            return details;

        } catch (Exception e) {
            log.error("Error in getOrderDetails(). userId: {}, orderId: {}", userId, orderId, e);
            throw e;
        }
    }

    @Transactional
    public boolean cancelOrder(int userId, String orderId, String reason) {
        try {
            OrderedDetails order = orderDao.findByOrderId(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found");
            }
            if (order.getUserId() != userId) {
                throw new IllegalArgumentException("Unauthorized access to order");
            }
            if (!order.getStatus().equals("pending")) {
                throw new IllegalArgumentException("Only pending orders can be cancelled");
            }

            return orderDao.updateOrderStatus(order.getId(), "cancelled", reason);

        } catch (Exception e) {
            log.error("Error in cancelOrder(). userId: {}, orderId: {}", userId, orderId, e);
            throw e;
        }
    }

    private String generateOrderId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        return "ORD-" + timestamp;
    }

    // Helper DTO for order history response
    @lombok.Data
    public static class OrderHistoryResponseDto {
        private List<OrderHistoryDto> orders = new ArrayList<>();
        private PaginationDto pagination = new PaginationDto();
    }
}
