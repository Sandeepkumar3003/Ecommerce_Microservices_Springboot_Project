package com.ecommerce.order.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity
@Data
public class CartItem {

    @Id
    private String id = UUID.randomUUID().toString(); // UUID as String ID

    // User can have many CartItems
    private String userId;
    private String productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
