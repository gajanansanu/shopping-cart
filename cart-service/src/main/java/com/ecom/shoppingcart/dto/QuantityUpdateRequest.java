package com.ecom.shoppingcart.dto;

public class QuantityUpdateRequest {
    @jakarta.validation.constraints.Min(1)
    private int quantity;
    public QuantityUpdateRequest() {}
    public QuantityUpdateRequest(int quantity) { this.quantity = quantity; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
