package com.hyuk.coffeeserver.dto;

import com.hyuk.coffeeserver.entity.Category;

public class CreateCoffeeRequest {

    private final String name;
    private final Category category;
    private final Long price;

    public CreateCoffeeRequest(String name, Category category, Long price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Long getPrice() {
        return price;
    }
}
