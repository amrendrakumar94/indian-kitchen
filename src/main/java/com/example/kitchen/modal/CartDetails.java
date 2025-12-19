package com.example.kitchen.modal;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "dish_id" })
})
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "user_id", nullable = false)
    int userId;

    @Column(name = "dish_id", nullable = false)
    int dishId;

    @Column(name = "quantity", nullable = false)
    int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
