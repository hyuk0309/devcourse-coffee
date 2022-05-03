package com.hyuk.coffeeserver.entity;

import java.util.UUID;

public class OrderItem {

    private final UUID coffeeId;
    private final Category category;
    private final long price;
    private final int quantity;

    public OrderItem(UUID coffeeId, Category category, long price, int quantity) {
        this.coffeeId = coffeeId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public UUID getCoffeeId() {
        return coffeeId;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
