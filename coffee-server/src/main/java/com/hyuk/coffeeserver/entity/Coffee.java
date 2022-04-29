package com.hyuk.coffeeserver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Coffee {

    private final UUID id;
    private String name;
    private Category category;
    private long price;
    private String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Coffee(UUID id, String name, Category category, long price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Coffee(UUID id, String name, Category category, long price, String description,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
