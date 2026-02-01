package com.ecommerce.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private String id = UUID.randomUUID().toString(); // UUID as String ID

    private String productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
