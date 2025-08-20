package com.ecommerce.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}
