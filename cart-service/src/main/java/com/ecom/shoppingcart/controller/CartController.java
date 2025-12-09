package com.ecom.shoppingcart.controller;

import com.ecom.shoppingcart.dto.CartItemRequest;
import com.ecom.shoppingcart.dto.CartItemResponse;
import com.ecom.shoppingcart.dto.QuantityUpdateRequest;
import com.ecom.shoppingcart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cart/items")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "List items in cart")
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> list() {
        return ResponseEntity.ok(cartService.listCartItems());
    }

    @Operation(summary = "Add product to cart")
    @PostMapping
    public ResponseEntity<CartItemResponse> add(@Valid @RequestBody CartItemRequest request) {
        CartItemResponse resp = cartService.addToCart(request);
        return ResponseEntity.created(URI.create("/api/cart/items/" + resp.getProductId())).body(resp);
    }

    @Operation(summary = "Update product in the cart")
    @PutMapping("/{productId}")
    public ResponseEntity<CartItemResponse> update(@PathVariable Long productId,
                                                   @Valid @RequestBody QuantityUpdateRequest request) {
        CartItemResponse resp = cartService.updateQuantity(productId, request.getQuantity());
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Remove product from cart")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return ResponseEntity.noContent().build();
    }

}

