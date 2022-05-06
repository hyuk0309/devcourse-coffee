package com.hyuk.coffeeserver.dto;

import java.util.UUID;

public class OrderItemDto {

    private UUID coffeeId;
    private String category;
    private long price;
    private int quantity;

    public OrderItemDto(UUID coffeeId, String category, long price, int quantity) {
        this.coffeeId = coffeeId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public UUID getCoffeeId() {
        return coffeeId;
    }

    public String getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
