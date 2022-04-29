package com.hyuk.coffeeserver.service;

import com.hyuk.coffeeserver.entity.Category;
import com.hyuk.coffeeserver.entity.Coffee;

public interface CoffeeService {

    Coffee createCoffee(String name, Category category, long price);
}
