package com.ecommerce.order.services;


import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.dtos.ProductResponse;
import com.ecommerce.order.dtos.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    //    @Autowired
//    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
//
//    private UserRepository userRepository;
//
////    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
//    public boolean addToCart(String userId, CartItemRequest request) {
////        Look for product
////        Optional<Product> productOpt = productRepository.findById(request.getProductID());
////        if(productOpt.isEmpty()){
////            return false;
////        }
//
//        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
//        System.out.println("Product Response: "+ productResponse);
//        if(productResponse == null || productResponse.getStockQuantity() < request.getQuantity()){
//            return false;
//        }
////
////        if(productResponse.getStockQuantity() < request.getQuantity()){
////            return false;
////        }
//////
////        Product product = productOpt.get();
////        if(product.getStockQuantity() < request.getQuantity()){
////            return false;
////        }
////
////        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
////        if(userOpt.isEmpty()){`
////            return false;
////        }
////
////        User user = userOpt.get();
//
////        if product already exist in UserCart then update the Quantity or else add new product in cart
//
//        UserResponse userResponse = userServiceClient.getUserDetails(userId);
//        System.out.println("UserResponse :" +userResponse);
//        if(userResponse == null){
//            return false;
//        }
//
//        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
//
//        if (existingCartItem != null) {
////           update the quantity
//            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
//            cartItemRepository.save(existingCartItem);
//
//        } else {
////            Create new Cart Item
//            CartItem cartItem = new CartItem();
//            cartItem.setUserId(userId);
//            cartItem.setProductId(request.getProductId());
//            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(BigDecimal.valueOf(100));
//            cartItem.setTotalPrice(BigDecimal.valueOf(1000.00));
//            cartItemRepository.save(cartItem);
//        }
//
//        return true;
//    }


@CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
public boolean addToCart(String userId, CartItemRequest request) {
    // Fetch product from product service
    ProductResponse productResponse;

    productResponse = productServiceClient.getProductDetails(request.getProductId());
    System.out.println("Product Response: "+ productResponse);


    if (productResponse == null || productResponse.getStockQuantity() < request.getQuantity()) {
        return false; // Product doesn't exist or insufficient stock
    }

    // Fetch user from user service
    UserResponse userResponse;

    userResponse = userServiceClient.getUserDetails(userId);
    System.out.println("UserResponse Response: "+ userResponse);


    if (userResponse == null) {
        return false; // User doesn't exist
    }

    // Check if cart item exists
    CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

    if (existingCartItem != null) {
        // Update existing cart item
        System.out.println("Existing quantity :"+ existingCartItem.getQuantity() +" requested quantity :"+ request.getQuantity());

        existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());

        System.out.println("Existing quantity :"+ existingCartItem.getQuantity() +" requested quantity :"+ request.getQuantity());
        existingCartItem.setPrice(productResponse.getPrice());
        existingCartItem.setTotalPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
        cartItemRepository.save(existingCartItem);
    } else {
        // Create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(productResponse.getPrice());
        cartItem.setTotalPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        cartItemRepository.save(cartItem);
//        return true;
    }

    return true;
}




    public boolean addToCartFallBack(String userId, CartItemRequest request,
                                     Exception exception){
        System.out.println("FALLBACK CALLED");
        return false;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        if(productOpt.isEmpty()){
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){
//            return false;
//        }
//
//        userOpt.flatMap( user ->
//                productOpt.map(product -> {
//                    cartItemRepository.deleteByUserAndProduct(user,product);
//                    return true;
//                })
//        );
//        return true;
//
//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;

    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
