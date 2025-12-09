package com.ecom.shoppingcart.repo;

import com.ecom.shoppingcart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductId(Long productId);
    void deleteByProductId(Long productId);
    boolean existsByProductId(Long productId);
}
