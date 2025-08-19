package com.ecommerce.order.services;


import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
//    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
//    private UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
//        Look for product
//        Optional<Product> productOpt = productRepository.findById(request.getProductID());
//        if(productOpt.isEmpty()){
//            return false;
//        }
////
//        Product product = productOpt.get();
//        if(product.getStockQuantity() < request.getQuantity()){
//            return false;
//        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){
//            return false;
//        }
//
//        User user = userOpt.get();

//        if product already exist in UserCart then update the Quantity or else add new product in cart

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
//           update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);

        } else {
//            Create new Cart Item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }

        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
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

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if (productOpt.isPresent() && userOpt.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }
        return false;

    }

    public List<CartItem> getCart(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }

    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(user ->
                cartItemRepository.deleteByUser(user)
        );
    }
}
