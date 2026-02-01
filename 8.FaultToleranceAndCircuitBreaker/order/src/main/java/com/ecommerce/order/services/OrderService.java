package com.ecommerce.order.services;

import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dtos.OrderItemDTO;
import com.ecommerce.order.dtos.OrderResponse;
import com.ecommerce.order.dtos.UserResponse;
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
    private UserServiceClient userServiceClient;
//
//    public Optional<OrderResponse> createOrder(String userId) {
////        Validate for Cart Item
////        Validate for User
////        Calculate total price
////        Create Order
////        Clear the cart
//
//        List<CartItem> cartItems = cartService.getCart(userId);
//        if (cartItems.isEmpty()) {
//            return Optional.empty();
//        }
//
//        System.out.println("CartItem :"+cartItems);
//
////        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
////        if (userOptional.isEmpty()) {
////            return Optional.empty();
////        }
////
////        User user = userOptional.get();
//
////        BigDecimal totalPrice = cartItems.stream()
////                .map(CartItem::getPrice)
////                .reduce(BigDecimal.ZERO, BigDecimal::add);
////        For above, we took list of cartItems and converted into streams and collected all the price of individual cart Item
////        used reduce method to perform some operation to add the values
//
//
////        Create Order
//        Order order = new Order();
//        order.setUserId(userId);
//        order.setStatus(OrderStatus.CONFIRMED);
//
//        order.setTotalAmount(totalPrice);
//
//        List<OrderItem> orderItems = cartItems.stream()
//                .map(item -> new OrderItem(
//                        null,
//                        item.getProductId(),
//                        item.getQuantity(),
//                        item.getPrice(),
//                        item.getTotalPrice(),
//                        order
//                )).toList();
//
//        System.out.println("OrderItem :"+orderItems);
//
//
//
//        order.setItems(orderItems);
//
//        System.out.println("Order  :"+order);
//
//        Order savedOrder = orderRepository.save(order);
//
//        System.out.println("Order  :"+order);
////        clear the cart
//        cartService.clearCart(userId);
//
//
//        System.out.println("Order  :"+order);
////        Convert Order Entity into OderResponse
//        OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
//
//        System.out.println("OrderResponse  :"+orderResponse);
//
//        return Optional.of(orderResponse);
//    }



    public Optional<OrderResponse> createOrders(String userId){

        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        System.out.println("CartItem :"+cartItems);
//
//        UserResponse userResponse;
//        try {
//            userResponse = userServiceClient.getUserDetails(userId);
//            System.out.println("UserResponse Response: "+ userResponse);
//        } catch (Exception e) {
//            System.out.println("User service unavailable: " + e.getMessage());
//            return Optional.empty();
//        }


        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);

        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProductId(item.getProductId());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setTotalPrice(item.getTotalPrice());


                    return orderItem;
                        }).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);


        List<OrderItemDTO> orderItemDTOS = orderItems.stream()
                .map(item -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO.setProductId(item.getProductId());
                    orderItemDTO.setQuantity(item.getQuantity());
                    orderItemDTO.setPrice(item.getPrice());
                    orderItemDTO.setTotalPrice(item.getTotalPrice());

                    return orderItemDTO;
                }).toList();




        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setItems(orderItemDTOS);
        orderResponse.setCreatedAt(order.getCreatedAt());


//        clear the cart
        cartService.clearCart(userId);

        return Optional.of(orderResponse);
    }
}


