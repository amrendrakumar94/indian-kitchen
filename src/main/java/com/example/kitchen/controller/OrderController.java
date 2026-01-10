package com.example.kitchen.controller;

import com.example.kitchen.dto.*;
import com.example.kitchen.modal.User;
import com.example.kitchen.service.OrderManagementService;
import com.example.kitchen.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@CrossOrigin(origins = "*")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderManagementService orderManagementService;

    // Existing endpoints (for backward compatibility)
    @GetMapping("get-order-details/{userId}")
    public JSONArray getOrderList(@PathVariable int userId) {
        return orderService.getOrderList(userId);
    }

    @PostMapping("save-order-details")
    public boolean saveOrder(@RequestBody JSONObject jsonObj) {
        return orderService.saveOrder(jsonObj);
    }

    // New order management endpoints
    @PostMapping("orders/place")
    public ResponseEntity<ResponseDto> placeOrder(@RequestBody PlaceOrderRequest request) {
        try {
            int userId = getCurrentUserId();

            OrderResponseDto response = orderManagementService.placeOrder(userId, request);

            return ResponseDto.successResponse(response, "Order placed successfully");

        } catch (IllegalArgumentException e) {
            log.error("Validation error in placeOrder: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in placeOrder", e);
            return ResponseDto.errorResponse("Failed to place order", e.getMessage());
        }
    }

    @GetMapping("orders")
    public ResponseEntity<ResponseDto> getOrders(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String status) {
        try {
            int userId = getCurrentUserId();

            OrderManagementService.OrderHistoryResponseDto response = orderManagementService.getOrders(userId, page,
                    pageSize, status);

            return ResponseDto.successResponse(response, "Orders fetched successfully");

        } catch (Exception e) {
            log.error("Error in getOrders", e);
            return ResponseDto.errorResponse();
        }
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<ResponseDto> getOrderDetails(@PathVariable String orderId) {
        try {
            int userId = getCurrentUserId();

            OrderDetailsDto response = orderManagementService.getOrderDetails(userId, orderId);

            return ResponseDto.successResponse(response, "Order details fetched successfully");

        } catch (IllegalArgumentException e) {
            log.error("Validation error in getOrderDetails: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in getOrderDetails", e);
            return ResponseDto.errorResponse();
        }
    }

    @PutMapping("orders/{orderId}/cancel")
    public ResponseEntity<ResponseDto> cancelOrder(
            @PathVariable String orderId,
            @RequestBody CancelOrderRequest request) {
        try {
            int userId = getCurrentUserId();

            boolean cancelled = orderManagementService.cancelOrder(userId, orderId, request.getReason());

            if (cancelled) {
                return ResponseDto.successResponse(null, "Order cancelled successfully");
            } else {
                return ResponseDto.errorResponse("Failed to cancel order", "");
            }

        } catch (IllegalArgumentException e) {
            log.error("Validation error in cancelOrder: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in cancelOrder", e);
            return ResponseDto.errorResponse();
        }
    }

    private int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }
}
