package com.example.kitchen.service;

import com.example.kitchen.dao.CartDao;
import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dto.*;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.DishDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartDao cartDao;
    private final DishDao dishDao;

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 10;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.09"); // 9%
    private static final BigDecimal DELIVERY_CHARGE = new BigDecimal("40");
    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("500");

    @Transactional
    public AddToCartResponseDto addToCart(int userId, int productId, int quantity) {
        try {
            // Validate quantity
            if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
                throw new IllegalArgumentException("Quantity must be between " + MIN_QUANTITY + " and " + MAX_QUANTITY);
            }

            // Validate product exists and in stock
            DishDetails product = dishDao.getDishById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }
            if (product.getInStock() != null && !product.getInStock()) {
                throw new IllegalArgumentException("Product out of stock");
            }

            // Check if cart item already exists
            CartDetails existingCartItem = cartDao.findByUserIdAndDishId(userId, productId);

            if (existingCartItem != null) {
                // Update quantity
                int newQuantity = existingCartItem.getQuantity() + quantity;
                if (newQuantity > MAX_QUANTITY) {
                    throw new IllegalArgumentException("Maximum quantity exceeded. Max allowed: " + MAX_QUANTITY);
                }
                existingCartItem.setQuantity(newQuantity);
                cartDao.save(existingCartItem);
            } else {
                // Create new cart item
                CartDetails newCartItem = new CartDetails();
                newCartItem.setUserId(userId);
                newCartItem.setDishId(productId);
                newCartItem.setQuantity(quantity);
                newCartItem.setPrice(BigDecimal.valueOf(product.getPrice()));
                cartDao.save(newCartItem);
                existingCartItem = newCartItem;
            }

            // Build response
            CartItemDto cartItemDto = convertToCartItemDto(existingCartItem, product);
            CartSummaryDto summaryDto = calculateSummary(userId);

            return new AddToCartResponseDto(cartItemDto, summaryDto);

        } catch (Exception e) {
            log.error("Error in addToCart(). userId: {}, productId: {}", userId, productId, e);
            throw e;
        }
    }

    public CartResponseDto getCart(int userId) {
        try {
            List<CartDetails> cartItems = cartDao.findAllByUserId(userId);
            List<CartItemDto> cartItemDtos = new ArrayList<>();

            for (CartDetails cartItem : cartItems) {
                DishDetails product = dishDao.getDishById(cartItem.getDishId());
                if (product != null) {
                    CartItemDto itemDto = convertToCartItemDto(cartItem, product);
                    cartItemDtos.add(itemDto);
                }
            }

            CartSummaryDto summary = calculateSummary(userId);

            CartResponseDto response = new CartResponseDto();
            response.setItems(cartItemDtos);
            response.setSummary(summary);

            return response;

        } catch (Exception e) {
            log.error("Error in getCart(). userId: {}", userId, e);
            throw e;
        }
    }

    @Transactional
    public AddToCartResponseDto updateQuantity(int userId, int productId, int quantity) {
        try {
            // Validate quantity
            if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
                throw new IllegalArgumentException("Quantity must be between " + MIN_QUANTITY + " and " + MAX_QUANTITY);
            }

            // Find cart item
            CartDetails cartItem = cartDao.findByUserIdAndDishId(userId, productId);
            if (cartItem == null) {
                throw new IllegalArgumentException("Cart item not found");
            }

            // Update quantity
            cartItem.setQuantity(quantity);
            cartDao.save(cartItem);

            // Build response
            DishDetails product = dishDao.getDishById(productId);
            CartItemDto cartItemDto = convertToCartItemDto(cartItem, product);
            CartSummaryDto summaryDto = calculateSummary(userId);

            return new AddToCartResponseDto(cartItemDto, summaryDto);

        } catch (Exception e) {
            log.error("Error in updateQuantity(). userId: {}, productId: {}", userId, productId, e);
            throw e;
        }
    }

    @Transactional
    public CartSummaryDto removeItem(int userId, int productId) {
        try {
            boolean deleted = cartDao.deleteByUserIdAndDishId(userId, productId);
            if (!deleted) {
                throw new IllegalArgumentException("Failed to remove item from cart");
            }

            return calculateSummary(userId);

        } catch (Exception e) {
            log.error("Error in removeItem(). userId: {}, productId: {}", userId, productId, e);
            throw e;
        }
    }

    @Transactional
    public CartSummaryDto clearCart(int userId) {
        try {
            cartDao.deleteAllByUserId(userId);
            return new CartSummaryDto(); // Empty summary

        } catch (Exception e) {
            log.error("Error in clearCart(). userId: {}", userId, e);
            throw e;
        }
    }

    private CartItemDto convertToCartItemDto(CartDetails cartItem, DishDetails product) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductDescription(product.getDescription());
        dto.setProductImage(product.getImage());
        dto.setCategory(product.getCategory());
        dto.setPrice(cartItem.getPrice());

        // Set original price and discount
        if (product.getOriginalPrice() != null) {
            dto.setOriginalPrice(BigDecimal.valueOf(product.getOriginalPrice()));
        }
        dto.setDiscount(product.getDiscount());

        dto.setQuantity(cartItem.getQuantity());

        // Calculate subtotal
        BigDecimal subtotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        dto.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));

        dto.setInStock(product.getInStock() != null ? product.getInStock() : true);
        dto.setMaxQuantity(MAX_QUANTITY);

        return dto;
    }

    public CartSummaryDto calculateSummary(int userId) {
        List<CartDetails> cartItems = cartDao.findAllByUserId(userId);

        CartSummaryDto summary = new CartSummaryDto();
        summary.setTotalItems(cartItems.size());

        BigDecimal subtotal = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (CartDetails item : cartItems) {
            BigDecimal itemSubtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemSubtotal);
            totalQuantity += item.getQuantity();
        }

        summary.setTotalQuantity(totalQuantity);
        summary.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));

        // Calculate tax (9%)
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        summary.setTax(tax);

        // Calculate delivery charge (₹40, free above ₹500)
        BigDecimal deliveryCharge = subtotal.compareTo(FREE_DELIVERY_THRESHOLD) > 0
                ? BigDecimal.ZERO
                : DELIVERY_CHARGE;
        summary.setDeliveryCharge(deliveryCharge);

        // Discount (future: apply coupons)
        summary.setDiscount(BigDecimal.ZERO);

        // Calculate total
        BigDecimal total = subtotal.add(tax).add(deliveryCharge).subtract(summary.getDiscount());
        summary.setTotal(total.setScale(2, RoundingMode.HALF_UP));

        return summary;
    }
}
