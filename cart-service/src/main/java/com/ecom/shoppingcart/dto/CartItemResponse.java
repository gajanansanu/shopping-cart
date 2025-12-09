package com.ecom.shoppingcart.dto;

import java.math.BigDecimal;

public class CartItemResponse {
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;

    public CartItemResponse() {}

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
