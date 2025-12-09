package com.ecom.shoppingcart.service;

import com.ecom.shoppingcart.dto.CartItemRequest;
import com.ecom.shoppingcart.dto.CartItemResponse;
import com.ecom.shoppingcart.exception.BadRequestException;
import com.ecom.shoppingcart.exception.NotFoundException;
import com.ecom.shoppingcart.model.CartItem;
import com.ecom.shoppingcart.model.Product;
import com.ecom.shoppingcart.repo.CartItemRepository;
import com.ecom.shoppingcart.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    @Override
    public CartItemResponse addToCart(CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be > 0");
        }

        CartItem cartItem = cartItemRepository.findByProductId(product.getId())
                .orElseGet(() -> new CartItem(product.getId(), 0));

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItem);

        return toResponse(product, cartItem);
    }

    @Override
    public CartItemResponse updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be > 0");
        }

        CartItem cartItem = cartItemRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Item not in cart for productId: " + productId));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));

        return toResponse(product, cartItem);
    }


    @Override
    public void removeFromCart(Long productId) {
        if (!cartItemRepository.existsByProductId(productId)) {
            throw new NotFoundException("Item not in cart for productId: " + productId);
        }
        cartItemRepository.deleteByProductId(productId);
    }

    @Override
    public List<CartItemResponse> listCartItems() {
        return cartItemRepository.findAll().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product not found: " + item.getProductId()));
                    return toResponse(product, item);
                })
                .collect(Collectors.toList());
    }

    private CartItemResponse toResponse(Product product, CartItem item) {
        CartItemResponse resp = new CartItemResponse();
        resp.setProductId(product.getId());
        resp.setProductName(product.getName());
        resp.setProductPrice(product.getPrice());
        resp.setQuantity(item.getQuantity());
        return resp;
    }
}

