package com.ecom.shoppingcart.service;

import com.ecom.shoppingcart.dto.CartItemRequest;
import com.ecom.shoppingcart.dto.CartItemResponse;
import com.ecom.shoppingcart.exception.BadRequestException;
import com.ecom.shoppingcart.exception.NotFoundException;
import com.ecom.shoppingcart.model.CartItem;
import com.ecom.shoppingcart.model.Product;
import com.ecom.shoppingcart.repo.CartItemRepository;
import com.ecom.shoppingcart.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private CartService cartService;

    private final Product sampleProduct =
            new Product(10L, "Sample", BigDecimal.valueOf(12.5));

    @BeforeEach
    void setUp() {
        cartItemRepository = mock(CartItemRepository.class);
        productRepository = mock(ProductRepository.class);
        cartService = new CartServiceImpl(cartItemRepository, productRepository);
    }

    // addToCart

    @Test
    void addToCart_createsNewCartItem() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(cartItemRepository.findByProductId(10L)).thenReturn(Optional.empty());

        CartItemRequest req = new CartItemRequest(10L, 2);
        CartItemResponse resp = cartService.addToCart(req);

        assertEquals(10L, resp.getProductId());
        assertEquals(2, resp.getQuantity());
        assertEquals("Sample", resp.getProductName());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addToCart_existingItem_incrementsQuantity() {
        CartItem existing = new CartItem(10L, 3);

        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(cartItemRepository.findByProductId(10L)).thenReturn(Optional.of(existing));

        CartItemRequest req = new CartItemRequest(10L, 4);
        CartItemResponse resp = cartService.addToCart(req);

        assertEquals(7, resp.getQuantity());
        verify(cartItemRepository).save(existing);
    }

    @Test
    void addToCart_productNotFound_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        CartItemRequest req = new CartItemRequest(99L, 1);

        assertThrows(NotFoundException.class, () -> cartService.addToCart(req));
    }

    @Test
    void addToCart_invalidQuantity_throwsException() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        CartItemRequest req = new CartItemRequest(10L, 0);

        assertThrows(BadRequestException.class, () -> cartService.addToCart(req));
    }

    // updateQuantity

    @Test
    void updateQuantity_success() {
        CartItem item = new CartItem(10L, 3);

        when(cartItemRepository.findByProductId(10L)).thenReturn(Optional.of(item));
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));

        CartItemResponse resp = cartService.updateQuantity(10L, 5);

        assertEquals(5, resp.getQuantity());
        verify(cartItemRepository).save(item);
    }

    @Test
    void updateQuantity_productNotInCart_throwsException() {
        when(cartItemRepository.findByProductId(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> cartService.updateQuantity(10L, 5));
    }

    @Test
    void updateQuantity_invalidQuantity_throwsException() {
        assertThrows(BadRequestException.class,
                () -> cartService.updateQuantity(10L, 0));
    }

    @Test
    void updateQuantity_productNotFoundLater_throwsException() {
        CartItem item = new CartItem(10L, 2);

        when(cartItemRepository.findByProductId(10L)).thenReturn(Optional.of(item));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> cartService.updateQuantity(10L, 5));
    }

    // removeFromCart

    @Test
    void removeFromCart_success() {
        when(cartItemRepository.existsByProductId(10L)).thenReturn(true);

        cartService.removeFromCart(10L);

        verify(cartItemRepository).deleteByProductId(10L);
    }

    @Test
    void removeFromCart_itemNotFound_throwsException() {
        when(cartItemRepository.existsByProductId(10L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> cartService.removeFromCart(10L));
    }

    // listCartItems

    @Test
    void listCartItems_returnsPopulatedList() {
        CartItem item1 = new CartItem(10L, 5);
        CartItem item2 = new CartItem(20L, 2);

        Product product1 = new Product(10L, "Mouse", BigDecimal.valueOf(20));
        Product product2 = new Product(20L, "Keyboard", BigDecimal.valueOf(40));

        when(cartItemRepository.findAll()).thenReturn(List.of(item1, item2));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(20L)).thenReturn(Optional.of(product2));

        List<CartItemResponse> list = cartService.listCartItems();

        assertEquals(2, list.size());
        assertEquals("Mouse", list.get(0).getProductName());
        assertEquals("Keyboard", list.get(1).getProductName());
    }

    @Test
    void listCartItems_missingProductInDB_throwsException() {
        CartItem item1 = new CartItem(10L, 5);

        when(cartItemRepository.findAll()).thenReturn(List.of(item1));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.listCartItems());
    }

}
