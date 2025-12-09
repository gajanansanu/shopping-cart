package com.ecom.shoppingcart.service;

import com.ecom.shoppingcart.dto.CartItemRequest;
import com.ecom.shoppingcart.dto.CartItemResponse;

import java.util.List;

public interface CartService {
    CartItemResponse addToCart(CartItemRequest request);
    CartItemResponse updateQuantity(Long productId, int quantity);
    void removeFromCart(Long productId);
    List<CartItemResponse> listCartItems();
}
