package com.example.kitchen.controller;

import com.example.kitchen.dto.*;
import com.example.kitchen.modal.User;
import com.example.kitchen.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addToCart(@RequestBody AddToCartRequest request) {
        try {
            int userId = getCurrentUserId();

            // Validate request
            if (request.getProductId() == null) {
                return ResponseDto.errorResponse("productId is required", "Invalid request");
            }
            if (request.getQuantity() == null) {
                request.setQuantity(1);
            }

            AddToCartResponseDto response = cartService.addToCart(userId, request.getProductId(), request.getQuantity());

            String message = "Item added to cart successfully";
            return ResponseDto.successResponse(response, message);
        } catch (IllegalArgumentException e) {
            log.error("Validation error in addToCart: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in addToCart", e);
            return ResponseDto.errorResponse();
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getCart() {
        try {
            int userId = getCurrentUserId();
            CartResponseDto cart = cartService.getCart(userId);

            String message = cart.getItems().isEmpty() ? "Cart is empty" : "Cart fetched successfully";

            return ResponseDto.successResponse(cart, message);

        } catch (Exception e) {
            log.error("Error in getCart", e);
            return ResponseDto.errorResponse();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateQuantity(@RequestBody UpdateCartRequest request) {
        try {
            int userId = getCurrentUserId();

            // Validate request
            if (request.getProductId() == null || request.getQuantity() == null) {
                return ResponseDto.errorResponse("productId and quantity are required", "Invalid request");
            }

            AddToCartResponseDto response = cartService.updateQuantity(userId, request.getProductId(), request.getQuantity());

            return ResponseDto.successResponse(response, "Cart item updated successfully");

        } catch (IllegalArgumentException e) {
            log.error("Validation error in updateQuantity: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in updateQuantity", e);
            return ResponseDto.errorResponse();
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ResponseDto> removeItem(@PathVariable Integer productId) {
        try {
            int userId = getCurrentUserId();
            CartSummaryDto summary = cartService.removeItem(userId, productId);

            RemoveItemResponseDto response = new RemoveItemResponseDto();
            response.setCartSummary(summary);

            return ResponseDto.successResponse(response, "Item removed from cart successfully");

        } catch (IllegalArgumentException e) {
            log.error("Validation error in removeItem: {}", e.getMessage());
            return ResponseDto.errorResponse(e.getMessage(), "Validation error");
        } catch (Exception e) {
            log.error("Error in removeItem", e);
            return ResponseDto.errorResponse();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseDto> clearCart() {
        try {
            int userId = getCurrentUserId();
            CartSummaryDto summary = cartService.clearCart(userId);

            ClearCartResponseDto response = new ClearCartResponseDto();
            response.setCartSummary(summary);

            return ResponseDto.successResponse(response, "Cart cleared successfully");

        } catch (Exception e) {
            log.error("Error in clearCart", e);
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

    // Helper DTOs for response
    @lombok.Data
    private static class RemoveItemResponseDto {
        private CartSummaryDto cartSummary;
    }

    @lombok.Data
    private static class ClearCartResponseDto {
        private CartSummaryDto cartSummary;
    }
}
