package com.ecommerce.order.services;

import com.ecommerce.order.dtos.OrderResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    //    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public Optional<OrderResponse> createOrder(String userId) {
//        Validate for Cart Item
//        Validate for User
//        Calculate total price
//        Create Order
//        Clear the cart

        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//
//        User user = userOptional.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        For above, we took list of cartItems and converted into streams and collected all the price of individual cart Item
//        used reduce method to perform some operation to add the values

//        Create Order
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getTotalPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

//        clear the cart
        cartService.clearCart(userId);

//        Convert Order Entity into OderResponse
        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

        return Optional.of(orderResponse);
    }
}
